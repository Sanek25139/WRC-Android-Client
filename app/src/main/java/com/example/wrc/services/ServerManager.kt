package com.example.wrc.services


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wrc.data.RawCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.time.Duration.Companion.seconds

class ServerManager(private val serviceScope: CoroutineScope) : Closeable {


    private var socket: Socket = Socket()

    private var receiveJob: Job? = null
    private var pingPongJob: Job? = null

    var pongReceived = false
    private val _event = Event<RawCommand>().apply {
        subscribe {
            if (it.byteCommand == 0xFE.toByte())
                pongReceived = true
        }
    }

    private val _readingCommand = Event<Byte>()

    private lateinit var udpSocket: DatagramSocket
    private lateinit var serverAddress: InetAddress

    private var sequence: Long = 0

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

            socket.close()
            socket = Socket()

            activeEndPoint = endpoint
            socket.connect(endpoint)



            serverAddress = endpoint.address
            udpSocket = DatagramSocket()
            attemptReconnect = true

            startJob()
            connectionState = ConnectionState.Connected
            true
        } catch (_: Exception) {
            connectionState = ConnectionState.Error
            attemptReconnect = false

            false
        }
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
                5001
            )

            udpSocket.send(packet)
        }
    }

    fun disconnect() {
        socket.close()
        receiveJob?.cancel()
        pingPongJob?.cancel()
        connectionState = ConnectionState.Disconnected;
    }

    fun getNetInterface(scope: CoroutineScope) = NetInterface(::send,::sendUdp,_event,_readingCommand,scope)
    override fun close() {
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