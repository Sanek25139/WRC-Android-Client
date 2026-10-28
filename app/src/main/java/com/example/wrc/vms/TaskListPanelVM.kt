package com.example.wrc.vms
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.wrc.data.ProcessInfo
import kotlin.collections.emptySet

class TaskListPanelVM : ViewModel() {
    var process: Array<ProcessInfo> by mutableStateOf(emptyArray())

    fun expandWindow(handle: Long){

    }
    fun collapseWindow(handle: Long){

    }
    fun closeWindow(handle: Long){

    }
    fun stopProcess(pid: Int){

    }


}