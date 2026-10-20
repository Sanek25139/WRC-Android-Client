package com.example.wrc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(text: String, modifier: Modifier = Modifier, content: HeaderScope.() -> Unit): Unit{
    val scope = HeaderScope().apply(content)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp).clip(RoundedCornerShape(bottomEndPercent = 25, bottomStartPercent = 25))
            .background(Color.Blue)
    ) {
        Text(
            text,
            color = Color.White,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
        )

        scope.rightRowContent?.let {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {it()}
        }
        scope.leftRowContent?.let {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {it()}
        }
    }
}