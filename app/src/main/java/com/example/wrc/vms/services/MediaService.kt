package com.example.wrc.vms.services

import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrc.data.MediaData
import com.example.wrc.data.MediaPlaybackState
import com.example.wrc.data.RawCommand
import com.example.wrc.services.NetInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.time.Duration.Companion.milliseconds
@OptIn(ExperimentalSerializationApi::class)
class MediaService  (
    val net: NetInterface
) : ViewModel() {
    val json = Json{ prettyPrint = true}
    var title by mutableStateOf("")
    var artist by mutableStateOf("")
    var durationMs by mutableLongStateOf(0L)
    var currentMs by mutableLongStateOf(0L)

    var changedSeek by mutableStateOf(false)
    var seek: Long by mutableLongStateOf(0L);

    var mediaState by mutableStateOf(MediaPlaybackState.Unknown)

    var canNext by mutableStateOf(false)
    var canPrevious by mutableStateOf(false)
    var canSeek by mutableStateOf(false)

    var imageBitmap by mutableStateOf<ImageBitmap?>(null)

    private var progressJob: Job? = null

    init {
        net.receiveEvent.subscribe(::updateMediaData)
        net.receiveEvent.subscribe(::updateMediaState)
        net.receiveEvent.subscribe(::updateMediaPosition)
        net.receiveEvent.subscribe(::updateMediaThumbnail)
        net.receiveEvent.subscribe(::mediaClosed)
    }

    private fun updateMediaData(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x10.toByte()){
            val data = json.decodeFromStream<MediaData>(rawCommand.data.inputStream())

            title = data.title ?: "Неизвестный название"
            artist = data.artist ?: "Неизвестный исполнитель"
            durationMs = data.durationMs


        }
    }
    private fun updateMediaState(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x11.toByte()){
            val stateValue = ByteBuffer
                .wrap(rawCommand.data, 0, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
            val capabilities = ByteBuffer
                .wrap(rawCommand.data,4,4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
            val positionMs = ByteBuffer
                .wrap(rawCommand.data,8,8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .getLong()

            mediaState = MediaPlaybackState.fromValue(stateValue)
            if (mediaState == MediaPlaybackState.Playing) {

                currentMs = positionMs

                if (progressJob?.isActive != true)
                    startProgress()

            }
            else {

                progressJob?.cancel()
                progressJob = null
            }

            canNext = capabilities and 0x40 != 0
            canPrevious = capabilities and 0x80 != 0
            canSeek = capabilities and 0x8000 != 0
        }
    }
    private fun updateMediaPosition(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x12.toByte()){
            val positionMs = ByteBuffer
                .wrap(rawCommand.data,0,8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .getLong()
            currentMs = positionMs
        }
    }
    private fun updateMediaThumbnail(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x13.toByte()){
            if(rawCommand.data.isEmpty()){
                imageBitmap = null
                return
            }
            val bitmap = BitmapFactory.decodeByteArray(
                rawCommand.data,
                0,
                rawCommand.data.size
            )
            imageBitmap = bitmap.asImageBitmap()
        }
    }


    private  fun mediaClosed(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x14.toByte()){
            mediaState = MediaPlaybackState.Closed
        }
    }

    fun fastRewindSeek() {
        seek = (currentMs - 10000).coerceIn(0,durationMs)
        sendSeek(seek)
    }

    fun fastForwardSeek() {
        seek = (currentMs + 5000).coerceIn(0,durationMs)
        sendSeek(seek)
    }

    fun seekChangedFinished(){
        changedSeek = false
        sendSeek(seek)
    }
    fun sendSeek(value: Long){

        val data = ByteBuffer
            .allocate(Long.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putLong(value)
            .array()
        currentMs = value;
        net.sendTcp(0x05,data )
    }
    private fun startProgress() {
        progressJob = viewModelScope.launch {

            while (isActive) {
                currentMs = (currentMs + 1000).coerceAtMost(durationMs)
                delay(1000.milliseconds)
            }
        }
    }
}