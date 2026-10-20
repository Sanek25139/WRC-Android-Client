package com.example.wrc.data

import kotlinx.serialization.Serializable

@Serializable
data class MediaData(
    val title: String?,
    val artist: String?,
    val durationMs: Long,
)
