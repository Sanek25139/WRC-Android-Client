package com.example.wrc

import androidx.compose.ui.text.TextRange
import com.example.wrc.data.RawCommand
import javax.security.auth.callback.Callback

fun TextRange.isSingle(): Boolean = end == start
fun  TextRange.isSelect(): Boolean = end != start

fun <T> predicate(
    predicate: (T) -> Boolean,
    callback: (T) -> Unit
): (T) -> Unit {
    return { value ->
        if (predicate(value)) {
            callback(value)
        }
    }
}

fun predicateRawCommand(command: Byte, callback: (RawCommand) -> Unit) : (RawCommand) -> Unit {
    return predicate({it.byteCommand == command},callback);
}