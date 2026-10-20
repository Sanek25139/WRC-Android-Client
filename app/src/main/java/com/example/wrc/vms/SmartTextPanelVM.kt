package com.example.wrc.vms

import android.annotation.SuppressLint
import android.health.connect.datatypes.units.Length
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.wrc.data.RawCommand
import com.example.wrc.isSelect
import com.example.wrc.isSingle
import com.example.wrc.predicateRawCommand
import com.example.wrc.services.NetInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SmartTextPanelVM(val net: NetInterface, val onBack:() -> Unit ) : ViewModel() {



    init {
        net.receiveEvent.apply {
            subscribe(predicateRawCommand(0x50,::onFocus))
            subscribe(predicateRawCommand(0x51,::onNotFocus))
            subscribe(::onRead)
            subscribe(::onServerAddText)
            subscribe(::onServerRemoveText)
            subscribe(predicateRawCommand(0x55,::onCloseWrite))

        }
    }
    var textValue by mutableStateOf(TextFieldValue(""))

    var isFocus by mutableStateOf(false)
    var readOnly by mutableStateOf(false)
    var isReading by mutableStateOf(false)
        private set
    var isWritable by mutableStateOf(false)
        private set


    fun back() = onBack()

    fun changedTextValue(newValue: TextFieldValue){

        if(!readOnly && !isWritable){
            openWrite()
        }

        if(isWritable){
            val oldTextLength = textValue.text.length
            val newTextLength = newValue.text.length
            val newText = newValue.text

            if(newText.length == oldTextLength){
                setServerCursor(newValue.selection)
            }

            if(textValue.selection.end < newValue.selection.end && oldTextLength < newTextLength){
                val str = newText.substring(textValue.selection.end, newValue.selection.end)
                sendText(str)
            }

            if (textValue.selection.isSelect() && newValue.selection.isSingle() && newTextLength < oldTextLength) {
                sendBackspace(1,oldTextLength - newTextLength)
            }
            if (textValue.selection.isSingle() && newValue.selection.isSingle() && newTextLength < oldTextLength) {
                val count = oldTextLength - newTextLength
                sendBackspace(count,count)
            }
        }





        textValue = newValue
    }

    private fun setServerCursor(selection: TextRange){
        val data = ByteBuffer
            .allocate(8)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(selection.start+1)
            .putInt(selection.end+1)
            .array()

        net.sendTcp(0x59,data)
    }

    fun onCloseWrite(rawCommand: RawCommand){
        isWritable = false;
    }

    fun onFocus(raw: RawCommand){
        isFocus = true
    }

    fun onNotFocus(raw: RawCommand){
        isFocus = false
    }

    fun onRead(raw: RawCommand){
        if(raw.byteCommand == 0x56.toByte()){
            readOnly = raw.data[0] != 1.toByte()
            textValue = TextFieldValue( String(
                raw.data,
                1,
                raw.data.size - 1,
                Charsets.UTF_8
            ))
        }
    }
    fun onServerAddText(raw: RawCommand){
        if(raw.byteCommand == 0x57.toByte()){
            val pos = ByteBuffer
                .wrap(raw.data,0,4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
            val addText = String(raw.data, 4, raw.data.size - 4, Charsets.UTF_8)
            val text = textValue.text

            Log.d("SmartRead",addText)

            if (pos !in 0..text.length) return
            textValue = textValue.copy(
                text = text.replaceRange(pos, pos, addText),
                selection = TextRange(pos + addText.length)
            )
        }
    }
    fun onServerRemoveText(raw: RawCommand){
        if(raw.byteCommand == 0x58.toByte()){
            val buffer = ByteBuffer
                .wrap(raw.data)
                .order(ByteOrder.LITTLE_ENDIAN)

            val start = buffer.getInt(0)
            val end = buffer.getInt(4)

            val text = textValue.text

            if (start !in 0..text.length || end !in start..text.length) return

            textValue = textValue.copy(
                text = text.removeRange(start, end),
                selection = TextRange(start)
            )
        }
    }

    fun openRead() {
        if (isReading) return

        isReading = true
        net.sendTcp(0x52)
    }

    fun closeRead() {
        if (!isReading) return

        isReading = false
        net.sendTcp(0x53)
    }
    fun openWrite(){
        if(isWritable) return

        isWritable = true
        net.sendTcp(0x54)
        setServerCursor(textValue.selection)
    }
    fun closeWrite(){
        if(!isWritable) return

        isWritable = false
        net.sendTcp(0x55)
    }
    fun sendCombo(codes: ByteArray){
        net.sendTcp(0x42, codes)
    }
    fun sendText(text: String){
        val data = text.toByteArray(Charsets.UTF_8)
        net.sendTcp(0x40,data)
    }
    fun sendBackspace(count: Int, length: Int){
        val data = ByteBuffer
            .allocate(8)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(count)
            .putInt(length)
            .array()
        net.sendTcp(0x41,data)
    }
    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        closeRead()
        super.onCleared()
    }
}