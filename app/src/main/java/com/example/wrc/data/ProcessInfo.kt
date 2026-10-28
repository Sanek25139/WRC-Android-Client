package com.example.wrc.data

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.serialization.Serializable

@Serializable
data class ProcessInfo(val exeName: String, @Transient var exeIcon: ByteArray = byteArrayOf(), val pid: Int, val windows: List<WindowInfo>){
    fun getBitmap(): ImageBitmap {
        return BitmapFactory.decodeByteArray(
            exeIcon,
            0,
            exeIcon.size
        ).asImageBitmap()
    }
}

@Serializable
data class WindowInfo(val name: String, val handle: Long)
