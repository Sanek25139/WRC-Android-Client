package com.example.wrc.vms.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.media.VolumeProvider
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import com.example.wrc.data.MediaData
import com.example.wrc.data.MediaPlaybackState
import com.example.wrc.data.RawCommand
import com.example.wrc.predicateRawCommand
import com.example.wrc.services.NetInterface
import com.example.wrc.services.ServerManager
import com.example.wrc.services.di.DIContext
import com.example.wrc.services.di.IService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class MediaSessionService(
    private val context: Context,
    private val net: NetInterface,
    private val scope: CoroutineScope,
    private val onSessionChanged: () -> Unit,
    private val onPlayerClosed : () -> Unit) {
    private var session: MediaSession? = null
    private var mediaData: MediaData? = null
    private var mediaImage: Bitmap? = null

    private var currentState: Int = PlaybackState.STATE_NONE
    private var currentActions: Long = PlaybackState.ACTION_PLAY_PAUSE
    private var currentPositionMs: Long = 0

    private var isLocalVolumeChange = false
    private var pendingVolume: Int? = null
    private var volumeResetJob: Job? = null

    companion object {
        private const val MAX_VOLUME = 20
    }

    private var volumeProvider: VolumeProvider? = null
    val json = Json{ prettyPrint = true}

    init {
        net.receiveEvent.apply {
            subscribe(predicateRawCommand(0x10,::updateMediaData))
            subscribe(::updateMediaThumbnail)
            subscribe(::mediaClosed)
            subscribe(::updateMediaState)
            subscribe(::updateMediaPosition)
            subscribe(::updateVolume)
        }
    }


    fun getSessionToken(): MediaSession.Token? = session?.sessionToken
    fun getSession(): MediaSession? = session
    fun start() {
        if (session != null)
            return

        session = MediaSession(
            context,
            "WRC"
        ).apply {

            setCallback(object : MediaSession.Callback() {

                override fun onPlay() {
                    net.sendTcp(0x01.toByte())
                }

                override fun onPause() {
                    net.sendTcp(0x02.toByte())
                }

                override fun onSkipToNext() {
                    net.sendTcp(0x03.toByte())
                }

                override fun onSkipToPrevious() {
                    net.sendTcp(0x04.toByte())
                }
                override fun onSeekTo(pos: Long) {
                    val data = ByteBuffer
                        .allocate(Long.SIZE_BYTES)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putLong(pos)
                        .array()
                    net.sendTcp(0x05, data)

                    // оптимистично обновим позицию сразу, не дожидаясь эха от сервера
                    currentPositionMs = pos
                    pushPlaybackState()
                }
            })
            volumeProvider = createVolumeProvider(initialVolume = 0.5f)
            setPlaybackToRemote(volumeProvider!!)

            setPlaybackState(
                PlaybackState.Builder()
                    .setState(
                        PlaybackState.STATE_PAUSED,
                        0,
                        0f
                    )
                    .setActions(
                        PlaybackState.ACTION_PLAY_PAUSE or
                                PlaybackState.ACTION_SKIP_TO_NEXT or
                                PlaybackState.ACTION_SKIP_TO_PREVIOUS
                    )
                    .build()
            )


            setMetadata(
                MediaMetadata.Builder()
                    .putString(
                        MediaMetadata.METADATA_KEY_TITLE,
                        "Тестовый трек"
                    )
                    .putString(
                        MediaMetadata.METADATA_KEY_ARTIST,
                        "WRC"
                    )
                    .putLong(
                        MediaMetadata.METADATA_KEY_DURATION,
                        180000L
                    )
                    .build()
            )


            isActive = true
            Log.d("WRC_MEDIA", "active=$isActive")
            Log.d("WRC_MEDIA", "token=$sessionToken")


        }

    }

    fun stop() {
        session?.release()
        session = null
    }
    private fun createVolumeProvider(initialVolume: Float): VolumeProvider {
        return object : VolumeProvider(
            VOLUME_CONTROL_ABSOLUTE,
            MAX_VOLUME,
            (initialVolume * MAX_VOLUME).roundToInt()
        ) {
            override fun onSetVolumeTo(volume: Int) {
                val floatVolume = volume / MAX_VOLUME.toFloat()

                isLocalVolumeChange = true
                pendingVolume = volume
                sendVolume(floatVolume)
                currentVolume = volume

                // подстраховка: если эхо не придёт или не совпадёт — снимем блокировку через таймаут
                volumeResetJob?.cancel()
                volumeResetJob = scope.launch {
                    delay(1000.milliseconds)
                    isLocalVolumeChange = false
                    pendingVolume = null
                }
            }

            override fun onAdjustVolume(direction: Int) {
                val newVolume = (currentVolume + direction * 1).coerceIn(0, MAX_VOLUME)
                onSetVolumeTo(newVolume)
            }
        }
    }
    private fun updateVolume(rawCommand: RawCommand) {
        if (rawCommand.byteCommand != 0x20.toByte()) return

        val volume = ByteBuffer.wrap(rawCommand.data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).float
        val isMute = rawCommand.data[4] != 0.toByte()
        val displayVolume = if (isMute) 0 else (volume * MAX_VOLUME).roundToInt()

        if (isLocalVolumeChange) {
            if (displayVolume == pendingVolume) {
                // сервер подтвердил именно то значение, что мы отправили — снимаем блокировку
                isLocalVolumeChange = false
                pendingVolume = null
                volumeResetJob?.cancel()
            }
            // иначе игнорируем это входящее обновление — ждём подтверждения или таймаута
            return
        }

        volumeProvider?.currentVolume = displayVolume.coerceIn(0, MAX_VOLUME)
    }
    private  fun mediaClosed(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x14.toByte()){
            session?.isActive = false;
            onPlayerClosed();
        }
    }
    @OptIn(ExperimentalSerializationApi::class)
    private fun updateMediaData(rawCommand: RawCommand){
        Log.d("MediaSessionService", "updateMediaData")
        if(session != null){
            val data = json.decodeFromStream<MediaData>(rawCommand.data.inputStream())
            mediaData = data
            updateMetadata()
            session!!.isActive = true
        }
    }
    private fun updateMediaThumbnail(rawCommand: RawCommand){
        if(rawCommand.byteCommand == 0x13.toByte()){
            Log.d("MediaSessionService", "updateMediaThumbnail")
            if(rawCommand.data.isEmpty()){
                mediaImage = null
            }
            else{
                val bitmap = BitmapFactory.decodeByteArray(
                    rawCommand.data,
                    0,
                    rawCommand.data.size
                )
                mediaImage = bitmap
            }
            updateMetadata()
        }
    }


    private fun updateMetadata() {
        val data = mediaData ?: return

        val builder = MediaMetadata.Builder()
            .putString(MediaMetadata.METADATA_KEY_TITLE, data.title ?: "")
            .putString(MediaMetadata.METADATA_KEY_ARTIST, data.artist ?: "")

        val canSeek = currentActions and PlaybackState.ACTION_SEEK_TO != 0L
        if (canSeek && data.durationMs > 0) {
            builder.putLong(MediaMetadata.METADATA_KEY_DURATION, data.durationMs)
        } else {
            builder.putLong(MediaMetadata.METADATA_KEY_DURATION, -1L) // именно -1, не 0
        }

        mediaImage?.let {
            builder.putBitmap(
                MediaMetadata.METADATA_KEY_ALBUM_ART,
                it
            )
        }

        session?.setMetadata(builder.build())
        onSessionChanged()
    }
    private fun updateMediaState(rawCommand: RawCommand) {
        if (rawCommand.byteCommand != 0x11.toByte()) return

        Log.d("MediaSessionService", "updateMediaState")

        val stateValue = ByteBuffer.wrap(rawCommand.data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int
        val capabilities = ByteBuffer.wrap(rawCommand.data, 4, 4).order(ByteOrder.LITTLE_ENDIAN).int
        val positionMs = ByteBuffer.wrap(rawCommand.data, 8, 8).order(ByteOrder.LITTLE_ENDIAN).long

        val mediaState = MediaPlaybackState.fromValue(stateValue)

        var actions = PlaybackState.ACTION_PLAY_PAUSE
        if (capabilities and 0x40 != 0) actions = actions or PlaybackState.ACTION_SKIP_TO_NEXT
        if (capabilities and 0x80 != 0) actions = actions or PlaybackState.ACTION_SKIP_TO_PREVIOUS
        if (capabilities and 0x8000 != 0) actions = actions or PlaybackState.ACTION_SEEK_TO

        currentState = mediaState.toPlaybackState()
        currentActions = actions
        currentPositionMs = positionMs

        pushPlaybackState()
        onSessionChanged()
    }

    private fun updateMediaPosition(rawCommand: RawCommand) {
        if (rawCommand.byteCommand != 0x12.toByte()) return

        Log.d("MediaSessionService", "updateMediaPosition")
        val positionMs = ByteBuffer.wrap(rawCommand.data, 0, 8).order(ByteOrder.LITTLE_ENDIAN).long
        currentPositionMs = positionMs

        pushPlaybackState()
        onSessionChanged()
    }

    private fun pushPlaybackState() {
        val speed = if (currentState == PlaybackState.STATE_PLAYING) 1f else 0f
        session?.setPlaybackState(
            PlaybackState.Builder()
                .setState(currentState, currentPositionMs, speed)
                .setActions(currentActions)
                .build()
        )
    }

    private fun sendVolume(value: Float) {
        val data = ByteBuffer
            .allocate(Float.SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putFloat(value)
            .array()
        net.sendTcp(0x21, data)
    }

    private fun MediaPlaybackState.toPlaybackState(): Int = when (this) {
        MediaPlaybackState.Playing -> PlaybackState.STATE_PLAYING
        MediaPlaybackState.Paused -> PlaybackState.STATE_PAUSED
        MediaPlaybackState.Stopped -> PlaybackState.STATE_STOPPED
        MediaPlaybackState.Changing -> PlaybackState.STATE_BUFFERING
        MediaPlaybackState.Opened -> PlaybackState.STATE_STOPPED
        MediaPlaybackState.Closed, MediaPlaybackState.Unknown -> PlaybackState.STATE_NONE
    }
}