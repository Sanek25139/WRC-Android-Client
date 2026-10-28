package com.example.wrc.data

import kotlinx.serialization.Serializable

@Serializable
data class ServerData(val hostGUID: String, val hostName: String, val portUDP: Int, val systems: Int)
