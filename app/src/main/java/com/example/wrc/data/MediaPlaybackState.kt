package com.example.wrc.data

enum class MediaPlaybackState(val value: Int) {
    Unknown(0x0),

    Closed(0x1),

    Opened(0x2),

    Changing(0x3),

    Stopped(0x4),

    Playing(0x5),

    Paused(0x6);

    companion object {
        fun fromValue(value: Int): MediaPlaybackState =
            entries.firstOrNull { it.value == value }
                ?: Unknown
    }
}