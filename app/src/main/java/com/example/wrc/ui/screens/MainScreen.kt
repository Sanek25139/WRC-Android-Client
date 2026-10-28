package com.example.wrc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wrc.data.ComputerData
import com.example.wrc.ui.components.Header
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.MainScreenVM

@Composable
fun MainScreen(vm: MainScreenVM, navController: NavHostController){
    val backStackEntry by navController.currentBackStackEntryAsState()
    androidx.compose.runtime.LaunchedEffect(backStackEntry) {
        if (backStackEntry?.destination?.route == "main") vm.load()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header("WRC") {
            rightRow {
                IconButton(onClick = vm::searchForHosts) {
                    Icon(
                        AppIcons.Autorenew,
                        contentDescription = "Обновить",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { navController.navigate("add")}) {
                    Icon(
                        AppIcons.Add,
                        contentDescription = "Добавить",
                        tint = Color.White
                    )
                }
            }
        }
        Text("Сервера:")

        val groups = remember(vm.computers) {
            vm.computers
                .groupBy { it.serverGuid?.let { guid -> "guid:$guid" } ?: "address:${it.id}" }
                .values
                .map { hosts -> HostGroup(hosts.first().serverGuid, hosts) }
        }


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(groups, key = { it.guid ?: it.computers.first().id }) { group ->
                if (group.guid != null && group.computers.size > 1) {
                    OutlinedCard(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                        Column(Modifier.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = group.computers.first().serverName ?: group.computers.first().name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 15.dp)
                            )
                            group.computers.forEach { computer ->
                                ComputerCard(computer, { navController.navigate("edit/$it") }, vm::delete) {
                                    navController.navigate("remote/$it")
                                }
                            }
                        }
                    }
                } else {
                    group.computers.forEach { computer ->
                        ComputerCard(computer, { navController.navigate("edit/$it") }, vm::delete) {
                            navController.navigate("remote/$it")
                        }
                    }
                }
            }

        }

    }
}

private data class HostGroup(val guid: String?, val computers: List<ComputerData>)

@Composable
fun ComputerCard(data: ComputerData,onEdit: (String) -> Unit,onDelete: (String) -> Unit,onConnection: (String) -> Unit) {

    var expanded: Boolean by remember { mutableStateOf<Boolean>(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {

        Box(Modifier.padding(10.dp)) {
            Column(Modifier.align(Alignment.TopStart)) {
                Text(data.name, fontSize = 20.sp)
                Text("${data.ip}:${data.port}", fontSize = 20.sp, fontWeight = FontWeight(1))
                OutlinedButton(
                    onClick = {onConnection(data.id)},
                    Modifier.fillMaxWidth().height(50.dp).padding(top = 5.dp)
                        .padding(horizontal = 5.dp)
                ) {
                    Text("Подключится")
                }
            }
            IconButton(onClick = { expanded = true }, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(
                    AppIcons.MoreVert,
                    contentDescription = "Действия",
                )
                DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Настроить") },
                        leadingIcon = { Icon(AppIcons.Settings, null) },
                        onClick = {
                            expanded = false
                            onEdit(data.id)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Удалить") },
                        leadingIcon = { Icon(AppIcons.Delete, null) },
                        onClick = {
                            expanded = false
                            showDeleteDialog = true;
                        }
                    )
                }
            }
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                    },
                    title = {
                        Text("Удалить компьютер?")
                    },
                    text = {
                        Text("Вы действительно хотите удалить этот компьютер?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                onDelete(data.id)
                            }
                        ) {
                            Text("Удалить")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Отмена")
                        }
                    }
                )
            }
        }
    }
}
