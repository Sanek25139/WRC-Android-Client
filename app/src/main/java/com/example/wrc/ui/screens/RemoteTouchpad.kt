package com.example.wrc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.wrc.ui.components.ButtonPress
import com.example.wrc.vms.RemoteTouchpadVM
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun RemoteTouchpad(vm: RemoteTouchpadVM){

    var lastScrollTime = 0L
    Box(Modifier.fillMaxSize()){
        Box(
            Modifier.fillMaxSize().padding(end=70.dp).pointerInput(Unit){
                awaitPointerEventScope{
                    while (true){
                        val event = awaitPointerEvent()
                        vm.handlePoint(event)
                    }
                }
            }
        )
        Box(Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(40.dp).padding(bottom = 95.dp, top = 15.dp, end = 15.dp).background(Color.Gray)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    val now = System.currentTimeMillis()

                    if (now - lastScrollTime >= 20) {
                        vm.scroll(dragAmount.toInt().coerceIn(-1, 1))
                        lastScrollTime = now
                    }
                }
            }
        )

        Row(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(60.dp), Arrangement.spacedBy(10.dp)) {

            ButtonPress(onDown = {vm.downMouse(0.toByte())} ,onUp = {vm.upMouse(0.toByte())}, modifier = Modifier.weight(1f).background(Color.Gray).fillMaxSize()){
                Text("ЛКМ",Modifier.align(Alignment.Center))
            }
            ButtonPress(onDown = {vm.downMouse(1.toByte())} ,onUp = {vm.upMouse(1.toByte())}, modifier =  Modifier.weight(1f).background(Color.Gray).fillMaxSize()){
                Text("ПКМ",Modifier.align(Alignment.Center))
            }
        }
    }


}