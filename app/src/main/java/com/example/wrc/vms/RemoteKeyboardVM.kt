package com.example.wrc.vms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.wrc.isSelect
import com.example.wrc.isSingle
import com.example.wrc.services.NetInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RemoteKeyboardVM(val net: NetInterface) : ViewModel() {

    var keyboardVisibility: Boolean by mutableStateOf(false)

    val focusRequester = FocusRequester()

    var isCombo: Boolean by mutableStateOf(false)
    val comboKeys = mutableListOf<ToggleData>()

    //var text: String by mutableStateOf("")
    var textFieldValue by mutableStateOf(TextFieldValue(""))


    val systemKey = listOf<ToggleData>(
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

    fun getServerText(){
        net.sendTcp(0x46)
    }




//    fun onSmartInputCommandProcessing(raw: RawCommand){
//        if(raw.byteCommand == 0x50.toByte()){
//            serverInput = true
//            when (raw.data[0]){
//                0x00.toByte() -> isSmartInput = raw.data[1] == 1.toByte()
//
//
//                0x10.toByte() -> {
//
//                    serverStartPos = ByteBuffer
//                        .wrap(raw.data,1,4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//                    serverEndPos = ByteBuffer
//                        .wrap(raw.data,5,4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//
//                    textFieldValue = textFieldValue.copy(selection = TextRange(serverStartPos,serverEndPos))
//                }
//                0x11.toByte() -> {
//                    val cursorPos = ByteBuffer
//                        .wrap(raw.data,1,4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//
//
//
//
//                    textFieldValue = textFieldValue.copy(
//                        text =  String(raw.data, 5, raw.data.size - 5, Charsets.UTF_8),
//                        TextRange(cursorPos))
//
//                    loadingSmartText = false;
//                }
//                0x12.toByte() -> {
//                    serverInput = true
//                    val cursorPos = ByteBuffer
//                        .wrap(raw.data, 1, 4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//
//                    val text = String(
//                        raw.data,
//                        5,
//                        raw.data.size - 5,
//                        Charsets.UTF_8
//                    )
//                    textFieldValue = TextFieldValue(
//                        text = text,
//                        selection = TextRange(cursorPos)
//                    )
//                }
//                0x13.toByte() -> {
//                    serverInput = true
//                    var cursorPos = ByteBuffer
//                        .wrap(raw.data, 1, 4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//
//                    val text = String(
//                        raw.data,
//                        5,
//                        raw.data.size - 5,
//                        Charsets.UTF_8
//                    )
//
//                    if (textFieldValue.text.isEmpty()) {
//                        textFieldValue = textFieldValue.copy(
//                            text = text,
//                            selection = TextRange(text.length)
//                        )
//                        return
//                    }
//
//                    Log.d("SmartInput","${cursorPos.toString()} ${text.length}")
//                    if(cursorPos > textFieldValue.text.length)
//                        cursorPos = textFieldValue.text.length
//                    val newText = textFieldValue.text.replaceRange(
//                        cursorPos,
//                        cursorPos,
//                        text
//                    )
//
//                    textFieldValue = textFieldValue.copy(
//                        text = newText,
//                        selection = TextRange(cursorPos + text.length)
//                    )
//                }
//                0x14.toByte() -> {
//                    Log.d("SmartInput","0x14")
//                    val start = ByteBuffer
//                        .wrap(raw.data, 1, 4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//
//                    val end = ByteBuffer
//                        .wrap(raw.data, 5, 4)
//                        .order(ByteOrder.LITTLE_ENDIAN)
//                        .int
//
//                    val newText = textFieldValue.text.removeRange(start, end)
//
//                    textFieldValue = textFieldValue.copy(
//                        text = newText,
//                        selection = TextRange(start)
//                    )
//                }
//
//            }
//            serverInput = false
//
//        }
//    }

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



    fun changeValue(newValue: TextFieldValue) {




        val oldTextLength = textFieldValue.text.length
        val newTextLength = newValue.text.length
        val newText = newValue.text

        if (isCombo && newTextLength > oldTextLength) {
            val code = newText.last().uppercaseChar().code

            if (code in 0x30..0x39 || code in 0x41..0x5A) {
                perform(code.toByte())
            } else {
                perform()
            }

            textFieldValue = newValue
            return
        }

        if(newText.length == oldTextLength){

            val selection = newValue.selection

            val data = ByteBuffer
                .allocate(9)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(0x20)
                .putInt( selection.start)
                .putInt( selection.end)
                .array()

            net.sendTcp(0x50,data)


        }

        if(textFieldValue.selection.end < newValue.selection.end && oldTextLength < newTextLength){
            val str = newText.substring(textFieldValue.selection.end, newValue.selection.end)
            sendText(str)
        }

        if (textFieldValue.selection.isSelect() && newValue.selection.isSingle() && newTextLength < oldTextLength) {
            sendBackspace(1)
        }
        if (textFieldValue.selection.isSingle() && newValue.selection.isSingle() && newTextLength < oldTextLength) {
            sendBackspace(oldTextLength - newTextLength)
        }

        textFieldValue = newValue
    }

    fun onKeyboardClose(){
        textFieldValue = textFieldValue.copy(
            text = ""
        )


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