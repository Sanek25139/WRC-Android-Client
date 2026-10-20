package com.example.wrc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.wrc.services.ConnectionState
import com.example.wrc.ui.components.Header
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.RemoteScreenVM
import com.example.wrc.vms.TypeRemotePanel


@Composable
fun RemoteScreen(vm: RemoteScreenVM, navController: NavHostController){
    var expanded: Boolean by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, vm) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    if (vm.panel == TypeRemotePanel.SmartTextView) {
                        vm.smartTextVM.closeRead()
                        vm.smartTextVM.closeWrite()

                    }
                }
                Lifecycle.Event.ON_START -> {
                    if (vm.panel == TypeRemotePanel.SmartTextView
                    ) {
                        vm.smartTextVM.openRead()
                    }
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Box(Modifier.fillMaxSize()){
        Column(Modifier.fillMaxSize()) {
            if(vm.panel.showTopBar) {
                Header(vm.nameServer) {
                    rightRow {
                        IconButton({expanded = !expanded}) {
                            Icon(
                                AppIcons.Menu,
                                contentDescription = "Функции",
                                tint = Color.White
                            )
                            DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("Смарт-текст") },
                                    leadingIcon = { Icon(AppIcons.TextSnippet, null) },
                                    onClick = {
                                        expanded = false
                                        vm.changePanel(TypeRemotePanel.SmartTextView)
                                    }
                                )
                            }
                        }
                    }
                    leftRow {
                        IconButton({}) {
                            Icon(AppIcons.MimoDisconnect, null)
                        }
                    }
                }
            }

            Box(Modifier.weight(1f)){
                vm.panel.content(vm)
            }
            if(vm.panel.showBottomBar){
                Box(Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(topEndPercent = 25, topStartPercent = 25))
                    .background(Color.Blue)){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BarButton(
                            AppIcons.Gamepad,
                            "Пульт",
                            Modifier.weight(1f),
                            {vm.changePanel(TypeRemotePanel.RemoteController)}
                        )
                        BarButton(
                            AppIcons.Keyboard,
                            "Клавиатура",
                            Modifier.weight(1f),
                            vm::openKeyboard
                        )
                        BarButton(
                            AppIcons.Touchpad,
                            "Тачпад",
                            Modifier.weight(1f),
                            {vm.changePanel(TypeRemotePanel.RemoteTouchpad)}
                        )
                    }
                }
            }




        }

        KeyboardPanel(vm.keyboardVM)

        if(vm.serverManager.connectionState == ConnectionState.Error){
            AlertDialog(
                onDismissRequest = {
                    // Не даём закрыть диалог тапом снаружи
                },
                title = {
                    Text("Соединение потеряно")
                },
                text = {
                    Text("Не удалось связаться с сервером.")
                },
                confirmButton = {
                    Button(onClick = vm::reconnect) {
                        Text("Переподключиться")
                    }
                },
                dismissButton = {
                    Button(onClick = {navController.navigate("main")}) {
                        Text("На главную")
                    }
                }
            )
        }



    }

}

@Composable
fun BarButton(icon: ImageVector, text: String, modifier:Modifier = Modifier, onClick:()-> Unit){
    IconButton(onClick, modifier = modifier ) {
        Column( horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom) {
            Icon(icon,text, tint = Color.White)
        }
    }
}
