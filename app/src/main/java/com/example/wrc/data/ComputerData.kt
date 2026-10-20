package com.example.wrc.data

import com.example.wrc.services.IFileEntity
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ComputerData( var name: String, var ip: String, var port: Int,val id: String = UUID.randomUUID().toString()) :
    IFileEntity{
    override fun getUUID(): String {
        return id;
    }
}

