package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrc.data.ComputerData
import com.example.wrc.services.NetInterface
import com.example.wrc.services.ServerManager
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService
import com.example.wrc.vms.services.AudioService
import com.example.wrc.vms.services.MediaService
import kotlinx.coroutines.launch
import java.net.InetSocketAddress

class RemoteScreenVM : ViewModel(), IService {
    private lateinit var computerData: ComputerData

    private  lateinit var mediaService: MediaService
    private  lateinit var audioService: AudioService
    lateinit var serverManager: ServerManager
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

        nameServer =  computerData.name


    }

    override fun inject(provider: DIContext) {
        serverManager = provider.get<ServerManager>()
        netInterface = serverManager.getNetInterface(viewModelScope)

        mediaService = MediaService(netInterface)
        audioService = AudioService(netInterface)



        controllerVM = RemoteControllerVM(mediaService,audioService, netInterface)
        touchpadVM = RemoteTouchpadVM(netInterface)
        keyboardVM = RemoteKeyboardVM(netInterface)
        smartTextVM = SmartTextPanelVM(netInterface,::back)

        audioService.seekVolumeChanged = true
    }

    fun reconnect() = viewModelScope.launch {
        audioService.seekVolumeChanged = false
        serverManager.reconnect()
    }
    fun connect() = viewModelScope.launch {
        audioService.seekVolumeChanged = false
        serverManager.connect(InetSocketAddress(computerData.ip, computerData.port))
    }
    fun requestAllData() {
        audioService.seekVolumeChanged = false
        netInterface.sendTcp(0x0F.toByte())
    }



    override fun onCleared() {

    }
}
