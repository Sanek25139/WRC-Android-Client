package com.example.wrc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.wrc.data.ProcessInfo
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.TaskListPanelVM

@Composable
fun TaskListPanel(vm: TaskListPanelVM){

    Box(){
        LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            items(vm.process) {
                ProcessCard(it,vm::stopProcess,vm::expandWindow, vm::collapseWindow,vm::closeWindow)
            }
        }
    }


}
@Composable
fun ProcessCard(process: ProcessInfo, onStop:(Int)-> Unit,expandWindow:(Long)-> Unit, collapseWindow:(Long)-> Unit,closeWindow:(Long)-> Unit){

    var expanded: Boolean by remember { mutableStateOf(false) }

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { if(process.windows.size > 1) {expanded = !expanded} else { expandWindow(process.windows[0].handle)} }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    bitmap = process.getBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = process.exeName,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            IconButton(
                onClick = { onStop(process.pid) }
            ) {
                Icon(AppIcons.Block, contentDescription = "Остановить")
            }
        }
        if(expanded){
            LazyColumn(){

                items(process.windows){
                    Text(it.name)
                    IconButton({expandWindow(it.handle)}) { Icon(AppIcons.Remove,null)}
                    IconButton({closeWindow(it.handle)}) { Icon(AppIcons.Close,null)}
                }
            }
        }

    }

}

