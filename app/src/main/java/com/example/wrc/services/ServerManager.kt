package com.example.wrc.services


import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wrc.data.HostAnnouncement
import com.example.wrc.data.DiscoveredHost
import com.example.wrc.data.RawCommand
import com.example.wrc.data.ServerData
import com.example.wrc.predicateRawCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketTimeoutException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.time.Duration.Companion.seconds

class ServerManager(private val serviceScope: CoroutineScope) : Closeable {

    val json = Json{ prettyPrint = true}
    private var socket: Socket = Socket()

    private var receiveJob: Job? = null
    private var pingPongJob: Job? = null

    var pongReceived = false
    private val _event = Event<RawCommand>().apply {
        subscribe {
            if (it.byteCommand == 0xFE.toByte())
                pongReceived = true
        }
        subscribe(predicateRawCommand(0x00,::onGetServerData))
    }

    private val _broadcastEvent = Event<DiscoveredHost>()
    private var hostDiscoveryJob: Job? = null
    @Volatile private var discoverySocket: DatagramSocket? = null
    @Volatile var expectedHostGuid: String? = null
        private set

    private val _readingCommand = Event<Byte>()

    private lateinit var udpSocket: DatagramSocket
    private lateinit var serverAddress: InetAddress

    private var udpPort: Int? = null

    private var sequence: Long = 0

    var serverData: ServerData? by mutableStateOf(null)
        private set

    var connectionState: ConnectionState by mutableStateOf(ConnectionState.Disconnected)
    var rtt by mutableLongStateOf(0L)


    var activeEndPoint: InetSocketAddress? = null

    var attemptReconnect: Boolean = true

    suspend fun checkConnection(): Boolean = withContext(Dispatchers.IO) {
        if (!socket.isConnected || socket.isClosed)
            return@withContext false
            try {
                val stream = socket.getOutputStream()

                val buffer = ByteBuffer
                    .allocate(5)
                    .order(ByteOrder.LITTLE_ENDIAN)

                buffer.put(0xFF.toByte())
                buffer.putInt(0)

                stream.write(buffer.array())
                stream.flush()
                true
            } catch (_: IOException) {
                false
            }
        }


    suspend fun connect(endpoint: InetSocketAddress): Boolean = withContext(Dispatchers.IO) {
        try {
            connectionState = ConnectionState.Connecting
            serverData = null

            socket.close()
            socket = Socket()

            activeEndPoint = endpoint
            socket.connect(endpoint)



            serverAddress = endpoint.address

            attemptReconnect = true

            startJob()
            delay(1.seconds)
            send(0x00,byteArrayOf())
            connectionState = ConnectionState.Connected

            true
        } catch (_: Exception) {
            connectionState = ConnectionState.Error
            attemptReconnect = false

            false
        }
    }

    fun subscribeToDiscoveredHosts(listener: (DiscoveredHost) -> Unit): EventToken =
        _broadcastEvent.subscribe(listener)

    fun expectHost(guid: String?) {
        expectedHostGuid = guid
    }

    fun onNetworkLost() {
        if (connectionState == ConnectionState.Connected ||
            connectionState == ConnectionState.Connecting
        ) {
            socket.close()
            receiveJob?.cancel()
            pingPongJob?.cancel()
            attemptReconnect = false
            connectionState = ConnectionState.Error
        }
        startHostDiscovery()
    }

    fun startHostDiscovery(durationMillis: Long = 30_000L) {
        require(durationMillis > 0) { "Discovery duration must be positive" }
        stopHostDiscovery()
        hostDiscoveryJob = serviceScope.launch(Dispatchers.IO) {
        val discoveryPort = 47777
        val json = Json { ignoreUnknownKeys = true }
        try {
        DatagramSocket(null).use { socket ->
            discoverySocket = socket
            socket.reuseAddress = true
            socket.bind(InetSocketAddress(discoveryPort))
            socket.soTimeout = 500
            socket.broadcast = true

            // The PC answers this short discovery request with HostAnnouncement JSON.
            val query = "WRC_DISCOVER_V1".encodeToByteArray()
            socket.send(
                DatagramPacket(
                    query,
                    query.size,
                    InetSocketAddress("255.255.255.255", discoveryPort)
                )
            )

            val buffer = ByteArray(2048)
            val deadline = System.nanoTime() + durationMillis * 1_000_000L

            try {
            while (!socket.isClosed && System.nanoTime() < deadline) {
                val packet = DatagramPacket(buffer, buffer.size)
                try {
                    socket.receive(packet)
                } catch (_: SocketTimeoutException) {
                    continue
                } catch (e: SocketException) {
                    if (socket.isClosed) break
                    throw e
                }

                val message = runCatching {
                    json.decodeFromString<HostAnnouncement>(
                        packet.data
                            .decodeToString(packet.offset, packet.offset + packet.length)
                    )
                }.getOrNull() ?: continue

                val discovered = DiscoveredHost(
                    message,
                    packet.address.hostAddress ?: continue
                )
                _broadcastEvent.invoke(discovered)

                if (message.hostGuid == expectedHostGuid &&
                    (connectionState == ConnectionState.Error || connectionState == ConnectionState.Disconnected)
                ) {
                    discoverySocket?.close()
                    connect(InetSocketAddress(discovered.ip, message.tcpPort))
                    break
                }
            }
            } finally {
                if (discoverySocket === socket) discoverySocket = null
            }
        }
        } catch (e: IOException) {
            // Network can disappear between ConnectivityManager callbacks and send().
            Log.d("Discovery", "Discovery stopped because the network is unavailable", e)
        }
        }
    }

    fun stopHostDiscovery() {
        hostDiscoveryJob?.cancel()
        hostDiscoveryJob = null
        discoverySocket?.close()
        discoverySocket = null
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun onGetServerData(raw: RawCommand){

        serverData = json.decodeFromStream<ServerData>(raw.data.inputStream())
        expectedHostGuid = serverData!!.hostGUID

        udpPort = serverData!!.portUDP
        udpSocket = DatagramSocket()
    }
    private fun startJob() {
        receiveJob?.cancel()
        pingPongJob?.cancel()

        receiveJob = serviceScope.launch {
            try {
                receiveLoop()
            } catch (_: IOException) {
                // соединение потеряно
            }
        }
        pingPongJob = serviceScope.launch {
            pingPong()
        }
    }

    private suspend fun receiveLoop() = withContext(Dispatchers.IO) {
        val stream = socket.getInputStream()

        while (true) {
            val command = stream.read().let {
                if (it == -1)
                    throw IOException("Connection closed")

                it.toByte()
            }



            _readingCommand.invoke(command)

            val lengthBytes = ByteArray(4)
            stream.readExactly(lengthBytes)

            val length = ByteBuffer
                .wrap(lengthBytes)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int

            val data = ByteArray(length)
            stream.readExactly(data)
            Log.d("Server","Command ${command.toString()}")
            _event.invoke(RawCommand(command,data))
        }
    }

    private fun InputStream.readExactly(buffer: ByteArray) {
        var offset = 0

        while (offset < buffer.size) {
            val count = read(buffer, offset, buffer.size - offset)

            if (count == -1) {
                throw IOException("Connection closed")
            }

            offset += count
        }
    }

    suspend fun reconnect(): Boolean = withContext(Dispatchers.IO) {
        if(activeEndPoint != null){
            return@withContext connect(activeEndPoint!!)
        }
        else
            false

    }

    suspend fun send(command: Byte, data: ByteArray, isRetry: Boolean = false){
        withContext(Dispatchers.IO) {
            try {
                val stream = socket.getOutputStream()

                val buffer = ByteBuffer
                    .allocate(5 + data.size)
                    .order(ByteOrder.LITTLE_ENDIAN)

                buffer.put(command)
                buffer.putInt(data.size)
                buffer.put(data)

                stream.write(buffer.array())
                stream.flush()
            }
            catch (_: IOException) {
                if(attemptReconnect && !isRetry){
                    if(reconnect()){
                        send(command,data)
                        return@withContext
                    }

                }
                connectionState = ConnectionState.Error
                attemptReconnect = false
            }
        }
    }

    suspend fun sendUdp(command: Byte, data: ByteArray) {
        withContext(Dispatchers.IO) {
            if(udpPort == null)
                return@withContext

            val currentSequence = sequence++
            val buffer = ByteBuffer
                .allocate(13 + data.size)
                .order(ByteOrder.LITTLE_ENDIAN)

            buffer.put(command)
            buffer.putLong(currentSequence)
            buffer.putInt(data.size)
            buffer.put(data)

            val packet = DatagramPacket(
                buffer.array(),
                buffer.position(),
                serverAddress,
                udpPort!!
            )

            udpSocket.send(packet)
        }
    }

    fun disconnect() {
        socket.close()
        receiveJob?.cancel()
        pingPongJob?.cancel()
        udpPort = null
        connectionState = ConnectionState.Disconnected
    }

    fun getNetInterface(scope: CoroutineScope) = NetInterface(::send,::sendUdp,_event,_readingCommand,scope)
    override fun close() {
        stopHostDiscovery()
        disconnect()
        attemptReconnect = true
        activeEndPoint = null
    }


    suspend fun pingPong() = withContext(Dispatchers.IO) {
        while (true) {
            pongReceived = false
            val start = System.nanoTime()

            send(0xFF.toByte(),byteArrayOf())

            delay(30.seconds)

            if(!pongReceived){
                disconnect()
                connectionState = ConnectionState.Error
                return@withContext
            }
            rtt = (System.nanoTime() - start) / 1_000_000
        }
    }
}
enum class ConnectionState {
    Disconnected,
    Connecting,
    Connected,
    Error
}
