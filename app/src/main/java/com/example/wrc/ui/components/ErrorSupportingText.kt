package com.example.wrc.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wrc.services.StateVar

@Composable
fun ErrorSupportingText(stateValue: StateVar<*>){
    if(stateValue.isError){
        Text(stateValue.errorMsg)
    }
}