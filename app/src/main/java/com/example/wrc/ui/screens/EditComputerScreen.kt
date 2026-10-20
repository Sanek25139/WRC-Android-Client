package com.example.wrc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wrc.ui.components.Header
import com.example.wrc.ui.components.StateTextField
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.EditComputerScreenVM

@Composable
fun EditComputerScreen(vm: EditComputerScreenVM, navController: NavHostController){
    Column {
        Header("Редактирование") {
            leftRow {
                IconButton(onClick = {navController.navigate("main")}) {
                    Icon(AppIcons.ArrowBack,"назад",tint = Color.White)
                }
            }
            rightRow {
                IconButton(onClick = { if (vm.save()) { navController.navigate("main") } } ) {
                    Icon(AppIcons.Save,"сохранить",tint = Color.White)
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {

            val modifierText = Modifier.fillMaxWidth().padding(horizontal = 10.dp)

            StateTextField(vm.hostName,"Имя хоста",modifierText)
            StateTextField(vm.ip,"IP",modifierText)
            StateTextField(vm.port,"Port",modifierText)
        }
    }

}