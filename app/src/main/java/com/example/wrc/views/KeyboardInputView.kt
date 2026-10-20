package com.example.wrc.views

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection

class KeyboardInputView(
    context: Context,
    private val onText: (String) -> Unit,
    private val onDelete: () -> Unit
) : View(context) {

    private var composingText = ""

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    override fun onCreateInputConnection(
        outAttrs: EditorInfo
    ): InputConnection {

        outAttrs.inputType = InputType.TYPE_CLASS_TEXT
        outAttrs.imeOptions = EditorInfo.IME_ACTION_NONE

        return object : BaseInputConnection(this, false) {

            override fun setComposingText(
                text: CharSequence,
                newCursorPosition: Int
            ): Boolean {

                val newText = text.toString()

                when {
                    newText.length > composingText.length -> {
                        val added = newText.substring(composingText.length)
                        onText(added)
                    }

                    newText.length < composingText.length -> {
                        onDelete()
                    }

                    newText != composingText -> {
                        // Пока просто считаем заменой текста
                        onText(newText)
                    }
                }

                composingText = newText

                return true
            }

            override fun sendKeyEvent(
                event: KeyEvent
            ): Boolean {

                if (
                    event.keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN
                ) {
                    onDelete()
                    return true
                }

                return super.sendKeyEvent(event)
            }
        }
    }
}