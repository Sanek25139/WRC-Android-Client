package com.example.wrc.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.wrc.services.StateVar

@Composable
fun StateTextField(
    state: StateVar<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = state::changeValue,
        label = { Text(label) },
        isError = state.isError,
        supportingText = {
            if(state.isError){
                Text(state.errorMsg,  color = Color.Red)
            }
        },
        modifier = modifier
    )
}