package com.example.wrc.data

@Suppress("ArrayInDataClass")
data class RawCommand(val byteCommand: Byte, val data: ByteArray)
