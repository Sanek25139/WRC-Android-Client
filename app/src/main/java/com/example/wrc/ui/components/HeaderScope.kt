package com.example.wrc.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

class HeaderScope {
    internal var leftRowContent: (@Composable RowScope.() -> Unit)? = null
    internal var rightRowContent: (@Composable RowScope.() -> Unit)? = null

    fun leftRow(content: @Composable RowScope.() -> Unit) {
        leftRowContent = content
    }

    fun rightRow(content: @Composable RowScope.() -> Unit) {
        rightRowContent = content
    }
}