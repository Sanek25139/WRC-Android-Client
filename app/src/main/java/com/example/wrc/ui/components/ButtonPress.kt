package com.example.wrc.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun ButtonPress(
    modifier: Modifier = Modifier,
    onDown: () -> Unit = {},
    onUp: () -> Unit = {},

    content: @Composable BoxScope.() -> Unit,

) {
    Box(
        modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown()

                onDown()

                waitForUpOrCancellation()

                onUp()
            }
        }
    ) {
        content()
    }
}