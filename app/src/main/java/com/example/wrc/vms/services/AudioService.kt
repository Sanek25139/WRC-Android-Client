package com.example.wrc.vms.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.wrc.data.RawCommand
import com.example.wrc.services.IEvent
import com.example.wrc.services.NetInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AudioService(val net: NetInterface) {
    var volume: Float by mutableFloatStateOf(0f);

    var isMute by mutableStateOf(false);
    var seekVolumeChanged: Boolean by mutableStateOf(false);




    init {
        net.receiveEvent.subscribe(::updateVolume)
    }


    private  fun updateVolume(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x20.toByte()){
            if(!seekVolumeChanged){
                volume = ByteBuffer
                    .wrap(rawCommand.data,0,4)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .getFloat()
            }

            isMute = rawCommand.data[4] != 0.toByte()
        }
    }
    fun seekVolume(values: Float){
        seekVolumeChanged = true
        volume = values
        val data = ByteBuffer
            .allocate(Float.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putFloat(values)
            .array()

        net.sendTcp(0x21,data)
    }
    fun sendSetMute(){
        net.sendTcp(0x22, byteArrayOf(if(!isMute) 1.toByte() else 0.toByte()) )
    }
}