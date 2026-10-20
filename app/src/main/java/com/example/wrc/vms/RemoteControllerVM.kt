package com.example.wrc.vms

import com.example.wrc.services.NetInterface
import com.example.wrc.vms.services.AudioService
import com.example.wrc.vms.services.MediaService


class RemoteControllerVM(
    val media: MediaService,
    val audio: AudioService,
    val net: NetInterface
)

fun Long.toMediaTime(): String {
    val totalSeconds = this / 1000

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}