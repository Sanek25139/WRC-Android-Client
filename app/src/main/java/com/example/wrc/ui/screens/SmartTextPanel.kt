package com.example.wrc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.SmartTextPanelVM

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SmartTextPanel(vm: SmartTextPanelVM){
    val imeVisible = WindowInsets.isImeVisible
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(imeVisible) {
        if(imeVisible){
            vm.openWrite()
        }
        else{
            vm.closeWrite()
        }
    }
    LaunchedEffect(vm.isWritable) {
        if(!vm.isWritable){
            keyboardController?.hide()
        }
    }
    Column(Modifier.fillMaxSize().imePadding()){
        Box(Modifier.fillMaxWidth().height(80.dp).statusBarsPadding()){
            IconButton(vm::back, Modifier.align(Alignment.CenterStart)) {
                Icon(AppIcons.ArrowBack,null) }
            Text("Имя контрола", Modifier.align(Alignment.Center))

        }
        if(vm.isFocus){
            TextField(value = vm.textValue,
                onValueChange = vm::changedTextValue,
                readOnly = vm.readOnly,
                modifier = Modifier.fillMaxSize().weight(1f))
        }
        else{
            Box(Modifier.fillMaxSize().weight(1f)){
                Text("Нет фокуса на допустимым элементе", Modifier.align(Alignment.Center))
            }
        }
        Row( horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(bottom = if(!imeVisible) 25.dp else 0.dp)
                .height(40.dp)) {
            ComboButton(AppIcons.Copy,vm::sendCombo,byteArrayOf(0x11, 0x43))
            ComboButton(AppIcons.CopyAll,vm::sendCombo,byteArrayOf(0x11, 0x41))
            ComboButton(AppIcons.Paste,vm::sendCombo,byteArrayOf(0x11, 0x56))
            ComboButton(AppIcons.Cut,vm::sendCombo,byteArrayOf(0x11, 0x58))
            ComboButton(AppIcons.Undo,vm::sendCombo,byteArrayOf(0x11, 0x5A))
            ComboButton(AppIcons.Redo,vm::sendCombo,byteArrayOf(0x11, 0x59))
        }
    }
}
@Composable
fun ComboButton(icon: ImageVector,onClick: (ByteArray) -> Unit,data:ByteArray){
    IconButton({onClick(data)}) {
        Icon(icon,null)
    }
}