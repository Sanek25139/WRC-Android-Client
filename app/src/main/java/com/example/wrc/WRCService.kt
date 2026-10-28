package com.example.wrc

import android.app.Service
import android.net.ConnectivityManager
import android.net.Network
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.snapshotFlow
import androidx.core.app.ServiceCompat
import com.example.wrc.services.ConnectionState
import com.example.wrc.services.NetInterface


import com.example.wrc.services.ServerManager
import com.example.wrc.vms.services.MediaSessionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WRCService : Service() {
    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )
    private lateinit var notificationHelper: WRCNotificationHelper
    lateinit var serverManager: ServerManager
        private set
    lateinit var net: NetInterface

    lateinit var mediaSessionService: MediaSessionService
        private set
    private lateinit var connectivityManager: ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (serverManager.connectionState != ConnectionState.Connected) {
                serverManager.startHostDiscovery()
            }
        }
        override fun onLost(network: Network) {
            serverManager.onNetworkLost()
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = WRCNotificationHelper(this)
        notificationHelper.createChannels()

        serverManager = ServerManager(serviceScope)
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        net = serverManager.getNetInterface(serviceScope)

        // ПОДСТАВЬ реальный способ подписки на статус соединения у ServerManager
        snapshotFlow { serverManager.connectionState }
            .distinctUntilChanged()
            .onEach { state: ConnectionState ->
                notificationHelper.updateConnectionNotification(state)
                if(state == ConnectionState.Disconnected || state == ConnectionState.Error || state == ConnectionState.Connecting)
                    notificationHelper.hideMediaNotification()
                if (state == ConnectionState.Error)
                    serverManager.startHostDiscovery()

             }
            .launchIn(serviceScope)

        mediaSessionService = MediaSessionService(this, net, serviceScope,
            onSessionChanged = { notificationHelper.showMediaNotification(mediaSessionService.getSession()) },
            onPlayerClosed = { notificationHelper.hideMediaNotification() }
        )
        mediaSessionService.start()

        instance.value = this
        // startForeground держится ИМЕННО на уведомлении о подключении
        val initial = notificationHelper.buildConnectionNotification(serverManager.connectionState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(this, WRCNotificationHelper.WRCNotifications.CONNECTION_ID, initial, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(WRCNotificationHelper.WRCNotifications.CONNECTION_ID, initial)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> mediaSessionService.getSession()?.controller?.transportControls?.play()
            "PAUSE" -> mediaSessionService.getSession()?.controller?.transportControls?.pause()
            "NEXT" -> mediaSessionService.getSession()?.controller?.transportControls?.skipToNext()
            "PREV" -> mediaSessionService.getSession()?.controller?.transportControls?.skipToPrevious()
            "DISCONNECT" -> serverManager.disconnect()
            "RECONNECT" -> serviceScope.launch {  serverManager.reconnect() }
            "EXIT" -> {
                serverManager.disconnect()
                stopSelf() // полностью останавливает сервис и убирает все уведомления
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        instance.value = null
        connectivityManager.unregisterNetworkCallback(networkCallback)

        //mediaSessionService.close()
        serverManager.close()

        super.onDestroy()
    }

    companion object {
        val instance = MutableStateFlow<WRCService?>(null)
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
