package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrc.data.ComputerData
import com.example.wrc.services.DataLocalService
import com.example.wrc.services.NetInterface
import com.example.wrc.services.ServerManager
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService
import com.example.wrc.vms.services.AudioService
import com.example.wrc.vms.services.MediaService
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.net.InetSocketAddress

class RemoteScreenVM : ViewModel(), IService {
    private lateinit var computerData: ComputerData

    private  lateinit var mediaService: MediaService
    private  lateinit var audioService: AudioService
    lateinit var serverManager: ServerManager
    private lateinit var dataLocalService: DataLocalService
    lateinit var netInterface: NetInterface
    lateinit var keyboardVM: RemoteKeyboardVM
    lateinit var controllerVM: RemoteControllerVM
    lateinit var touchpadVM: RemoteTouchpadVM
    lateinit var smartTextVM: SmartTextPanelVM

    var nameServer: String by mutableStateOf("")
    fun openKeyboard() = keyboardVM.openKeyboard()

    var panel:TypeRemotePanel by mutableStateOf(TypeRemotePanel.RemoteController)
        private set

    fun changePanel(typePanel: TypeRemotePanel) {
        if (panel == typePanel) return

        val oldPanel = panel
        oldPanel.onClose?.invoke(this)

        panel = typePanel

        typePanel.onOpen?.invoke(this)
    }
    fun back() = changePanel(TypeRemotePanel.RemoteController)


    fun load(data: ComputerData){
        computerData = data
        serverManager.expectHost(data.serverGuid)

        nameServer =  computerData.name


    }

    override fun inject(provider: DIContext) {
        serverManager = provider.get<ServerManager>()
        dataLocalService = provider.get<DataLocalService>()
        netInterface = serverManager.getNetInterface(viewModelScope)

        mediaService = MediaService(netInterface)
        audioService = AudioService(netInterface)



        controllerVM = RemoteControllerVM(mediaService,audioService, netInterface)
        touchpadVM = RemoteTouchpadVM(netInterface)
        keyboardVM = RemoteKeyboardVM(netInterface)
        smartTextVM = SmartTextPanelVM(netInterface,::back)

        audioService.seekVolumeChanged = true
    }

    fun disconnect() = viewModelScope.launch {
        serverManager.disconnect()
    }
    fun reconnect() = viewModelScope.launch {
        audioService.seekVolumeChanged = false
        serverManager.reconnect()
    }
    fun connect() = viewModelScope.launch {
        audioService.seekVolumeChanged = false
        if (serverManager.connect(InetSocketAddress(computerData.ip, computerData.port))) {
            identifyAndSaveHost()
        }
    }

    private suspend fun identifyAndSaveHost() {
        // Server metadata arrives asynchronously after connect() requests it.
        repeat(30) {
            val serverData = serverManager.serverData
            if (serverData != null) {
                dataLocalService.get<ComputerData>()
                dataLocalService.updateAndSave(computerData) {
                    it.serverGuid = serverData.hostGUID
                    it.serverName = serverData.hostName
                    it
                }
                computerData.serverGuid = serverData.hostGUID
                nameServer = serverData.hostName
                return
            }
            delay(100)
        }
    }
    fun requestAllData() {
        audioService.seekVolumeChanged = false
        netInterface.sendTcp(0x0F.toByte())
    }



    override fun onCleared() {

    }
}
