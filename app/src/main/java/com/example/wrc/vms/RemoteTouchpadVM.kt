package com.example.wrc.vms

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import com.example.wrc.services.NetInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RemoteTouchpadVM(val net: NetInterface) {

    var downPosition: Offset? = null
    fun handlePoint(event: PointerEvent){

        val  changePoint = mutableListOf<Pair<Float, Float>>()
        for (change in event.changes){


            if (change.changedToDown()) {
                downPosition = change.position
            }

            if (change.changedToUp()) {
                val start = downPosition

                if (start != null) {
                    val distance = (change.position - start).getDistance()

                    if (distance < 20f) {
                        clickMouse(0)
                    }
                }

                downPosition = null
            }


            val dx = change.position.x - change.previousPosition.x
            val dy = change.position.y - change.previousPosition.y

            changePoint.add(dx to dy)
        }

        val buffer = ByteBuffer
            .allocate((Float.SIZE_BYTES * 2) * event.changes.size)
            .order(ByteOrder.LITTLE_ENDIAN)

        changePoint.forEach { buffer.putFloat(it.first).putFloat(it.second) }
        net.sendUdp(0x30.toByte(),buffer.array())
    }

    fun clickMouse(buttonId: Byte){
        net.sendTcp(0x33.toByte(),byteArrayOf(buttonId))
    }
    fun downMouse(buttonId: Byte){
        net.sendTcp(0x31.toByte(),byteArrayOf(buttonId))
    }
    fun upMouse(buttonId: Byte){
        net.sendTcp(0x32.toByte(),byteArrayOf(buttonId))
    }
    fun scroll(value: Int){
        val data = ByteBuffer
            .allocate(Int.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(value)
            .array()
        net.sendUdp(0x34.toByte(),data)
    }

}