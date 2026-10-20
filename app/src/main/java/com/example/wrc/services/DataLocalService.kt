package com.example.wrc.services

import android.content.Context
import android.util.Log
import com.example.wrc.services.di.IService
import com.example.wrc.services.di.DIContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
class DataLocalService(build: DataLocalServiceScope.() -> Unit) : IService {
    val json = Json{ prettyPrint = true}
    lateinit var context:Context;
    val datas: Map<KClass<*>, LocalData<*>> = DataLocalServiceScope().apply(build).registrations

    override fun inject(provider: DIContext) {
        context = provider.get(Context::class)
        Log.d("WRC", context.filesDir.absolutePath)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T: IFileEntity> get(): List<T>{

        val metaData = datas[T::class] as LocalData<T>
        return metaData.data ?: load<T>(metaData.nameFile).also { metaData.data = it }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IFileEntity> addAndSave(data:T){
        val metaData = datas[T::class] as LocalData<T>
        val list = metaData.data
            ?: load<T>(metaData.nameFile)
                .also { metaData.data = it }
        list.add(data)
        save(metaData.nameFile, list)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IFileEntity> updateAndSave(oldData: T, update: (T) -> T){
        val metaData = datas[T::class] as LocalData<T>
        val list = metaData.data!!
        val index = list.indexOfFirst { it.getUUID() == oldData.getUUID() }
        if (index == -1)
            return
        list[index] = update(oldData)
        save(metaData.nameFile,list)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IFileEntity> deleteAndSava(data:T){
        val metaData = datas[T::class] as LocalData<T>
        metaData.data!!.removeIf { it.getUUID() == data.getUUID() }
        save<T>(metaData.nameFile, metaData.data!!);
    }
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IFileEntity> deleteAndSava(id: String){
        val metaData = datas[T::class] as LocalData<T>
        metaData.data!!.removeIf { it.getUUID() == id }
        save<T>(metaData.nameFile, metaData.data!!);
    }



    @PublishedApi
    internal inline fun <reified T : IFileEntity> save(
        nameFile: String,
        data: List<T>
    ) {
        val file = File(context.filesDir, nameFile)

        file.outputStream().use {
            json.encodeToStream(data, it)
        }
    }

    @PublishedApi
    internal inline fun <reified T : IFileEntity> load(nameFile: String): MutableList<T> {
        val file = File(context.filesDir, nameFile)

        if (!file.exists() || file.length() == 0L) {
            return mutableListOf()
        }
        return file.inputStream().use {
            json.decodeFromStream<MutableList<T>>(it)
        }
    }
}

data class LocalData<T : IFileEntity>(
    val nameFile: String,
    var data: MutableList<T>? = null
){
    companion object {
        inline fun <reified T : IFileEntity> create(
            nameFile: String
        ): LocalData<T> {
            return LocalData(
                nameFile = nameFile
            )
        }
    }
}

class DataLocalServiceScope {
    val registrations = mutableMapOf<KClass<*>, LocalData<*>>()

    inline fun <reified T: IFileEntity> fileData(nameFile: String) {
        registrations[T::class] = LocalData.create<T>(nameFile)
    }
}