package com.example.wrc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.wrc.ui.icons.AppIcons
import com.example.wrc.vms.RemoteKeyboardVM
import com.example.wrc.vms.ToggleData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KeyboardPanel(vm: RemoteKeyboardVM){
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.isImeVisible

    LaunchedEffect(imeVisible) {
        if (!imeVisible){
            vm.keyboardVisibility = false;
            vm.onKeyboardClose()
        }
    }
    val scrollState = rememberScrollState()
    var scale by remember { mutableFloatStateOf(1f) }
    val transformState = rememberTransformableState { zoomChange, _, _ ->
        scale = (scale * zoomChange).coerceIn(0.5f, 3f)
    }

    Box(Modifier.fillMaxSize().imePadding()){
        if (vm.keyboardVisibility) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(scrollState)
            ){
                TextField(
                    value = vm.textFieldValue,
                    onValueChange = vm::changeValue,

                    modifier = Modifier
                        .alpha(0f)
                        .size(1.dp)
                        .focusRequester(vm.focusRequester)
                        .onPreviewKeyEvent { event ->
                            if (event.key == Key.Backspace) {
                                if (event.type == KeyEventType.KeyDown) {
                                    vm.sendBackspace(1)
                                }
                                true
                            } else {
                                false
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (vm.isCombo) ImeAction.Done else ImeAction.Default
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            vm.perform()
                        }
                    )
                )
            }


            LaunchedEffect(Unit) {
                vm.focusRequester.requestFocus()
                keyboardController?.show()
            }
        }
        if (vm.keyboardVisibility) {
            LazyRow( horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(45.dp)) {
                items(vm.systemKey) {key -> ToggleButton(key,Modifier.fillMaxHeight().width(100.dp),{vm.pressSystemKey(key)})}
            }
        }


    }
}

@Composable
fun ToggleButton(vm: ToggleData,modifier: Modifier, onClick: () -> Unit){
    Button(onClick, shape = RectangleShape,modifier = modifier){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(vm.text)
            Box(Modifier
                .fillMaxWidth()
                .height(7.dp)
                .background(
                    if (vm.isPress) Color.Blue else Color.Transparent
                )
                .border(
                    2.dp,
                    if (vm.isPress) Color.Blue else Color.White
                )
            )
        }
    }
}