package com.example.wrc.vms

import androidx.compose.runtime.Composable
import com.example.wrc.ui.screens.RemoteController
import com.example.wrc.ui.screens.RemoteTouchpad
import com.example.wrc.ui.screens.SmartTextPanel

enum class TypeRemotePanel(
    val showBottomBar: Boolean,
    val showTopBar: Boolean,
    val content: @Composable (RemoteScreenVM) -> Unit,
    val onOpen: ((RemoteScreenVM) -> Unit)? = null,
    val onClose: ((RemoteScreenVM) -> Unit)? = null
) {
    RemoteController(
        true,
        true,
        { RemoteController(it.controllerVM) }
    ),

    RemoteTouchpad(
        true,
        true,
        { RemoteTouchpad(it.touchpadVM) }
    ),

    SmartTextView(
        false,
        false,
        { SmartTextPanel(it.smartTextVM) },
        onOpen = {it.smartTextVM.openRead()},
        onClose = {it.smartTextVM.closeRead();it.smartTextVM.closeWrite()},

    )
}