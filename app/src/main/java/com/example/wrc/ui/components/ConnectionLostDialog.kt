package com.example.wrc.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConnectionLostDialog(
    onReconnect: () -> Unit,
    onBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text("Соединение потеряно")
        },
        text = {
            Text("Соединение с сервером было потеряно.")
        },
        confirmButton = {
            TextButton(onClick = onReconnect) {
                Text("Переподключиться")
            }
        },
        dismissButton = {
            TextButton(onClick = onBack) {
                Text("На главную")
            }
        }
    )
}