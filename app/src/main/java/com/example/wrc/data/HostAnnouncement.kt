package com.example.wrc.data

import kotlinx.serialization.Serializable

@Serializable
data class HostAnnouncement(
    val hostGuid: String,
    val hostName: String,
    val tcpPort: Int
)