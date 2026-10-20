package com.example.wrc.services

import androidx.lifecycle.viewModelScope
import com.example.wrc.data.RawCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NetInterface(
    private val susSendTcp: suspend (command: Byte,data: ByteArray) -> Unit,
    private val susSendUdp: suspend (command: Byte,data: ByteArray) -> Unit,
    val receiveEvent: IEvent<RawCommand>,
    val readingEvent: IEvent<Byte>,
    private val scope: CoroutineScope
){

    fun sendTcp(command: Byte, data: ByteArray) {
        scope.launch {
            susSendTcp(command, data)
        }
    }
    fun sendTcp(command: Byte) {
        scope.launch {
            susSendTcp(command, byteArrayOf())
        }
    }
    fun sendUdp(command: Byte, data: ByteArray){
        scope.launch {
            susSendUdp(command, data)
        }
    }
}