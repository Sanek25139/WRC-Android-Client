package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import com.example.wrc.services.NetInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RemoteKeyboardVM(val net: NetInterface) : ViewModel() {

    var keyboardVisibility: Boolean by mutableStateOf(false)

    val focusRequester = FocusRequester()

    var isCombo: Boolean by mutableStateOf(false)
    val comboKeys = mutableListOf<ToggleData>()

    var text: String by mutableStateOf("")
    //var textFieldValue by mutableStateOf(TextFieldValue(""))


    val systemKey = listOf(
        ToggleData("WIN", 0x5B),
        ToggleData("SHIFT", 0x10),
        ToggleData("CTRL", 0x11),
        ToggleData("ALT", 0xA4.toByte()),

        ToggleData("BACK", 0x08),
        ToggleData("DELETE", 0x2E),
        ToggleData("SPACE", 0x20),
        ToggleData("TAB", 0x09),
        ToggleData("ESC", 0x1B),


        ToggleData("F1",0x70),
        ToggleData("F2",0x71),
        ToggleData("F3",0x72),
        ToggleData("F4",0x73),
        ToggleData("F5",0x74),
        ToggleData("F6",0x75),
        ToggleData("F7",0x76),
        ToggleData("F8",0x77),
        ToggleData("F9",0x78),
        ToggleData("F10",0x79),
        ToggleData("F11",0x7A),
        ToggleData("F12",0x7B),
    )

    fun openKeyboard() {
        keyboardVisibility = true
    }

    fun pressSystemKey(toggle: ToggleData){
        if(toggle.isPress) comboKeys.remove(toggle) else  comboKeys.add(toggle)
        toggle.isPress = !toggle.isPress
        isCombo = comboKeys.isNotEmpty()
    }
    fun perform(code: Byte? = null) {
        val data = ByteBuffer.allocate(
            comboKeys.size + if (code != null) 1 else 0
        )

        comboKeys.forEach {
            data.put(it.code)
        }

        code?.let {
            data.put(it)
        }

        net.sendTcp(0x42, data.array())

        comboKeys.forEach {
            it.isPress = false
        }

        comboKeys.clear()
        isCombo = false
    }



    fun changeValue(newText: String) {
        val oldTextLength = text.length
        val newTextLength = newText.length

        if (isCombo && newTextLength > oldTextLength) {
            val code = newText.last().uppercaseChar().code

            if (code in 0x30..0x39 || code in 0x41..0x5A) {
                perform(code.toByte())
            } else {
                perform()
            }

            text = newText
            return
        }

        if(oldTextLength < newTextLength)
        {
            sendText(newText.substring(oldTextLength))
        }

        if (newTextLength < oldTextLength) {
            sendBackspace(oldTextLength - newTextLength)
        }

        text = newText
    }

    fun onKeyboardClose(){
        text = ""


    }
    fun sendText(text: String){
        if(!keyboardVisibility) return
        val data = text.toByteArray(Charsets.UTF_8)
        net.sendTcp(0x40,data)
    }
    fun sendBackspace(count: Int){
        if(!keyboardVisibility) return
        val data = ByteBuffer
            .allocate(Int.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(count)
            .array()
        net.sendTcp(0x41,data)
    }
}
class ToggleData(val text: String, val code: Byte) : ViewModel(){
    var comboNum: Int? by mutableStateOf(null)
    var isPress: Boolean by mutableStateOf(false)
}