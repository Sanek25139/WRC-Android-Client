package com.example.wrc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.wrc.services.ConnectionState

class WRCNotificationHelper(private val context: Context) {

    object WRCNotifications {
        const val CHANNEL_CONNECTION = "wrc_connection"
        const val CHANNEL_MEDIA = "wrc_media"
        const val CONNECTION_ID = 1
        const val MEDIA_ID = 2
    }



    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun createChannels() {
        notificationManager.createNotificationChannel(
            NotificationChannel(WRCNotifications.CHANNEL_CONNECTION, "Подключение", NotificationManager.IMPORTANCE_LOW)
        )
        notificationManager.createNotificationChannel(
            NotificationChannel(WRCNotifications.CHANNEL_MEDIA, "Медиа", NotificationManager.IMPORTANCE_LOW)
        )
    }



    private fun buildMediaNotification(session: MediaSession?): Notification {
        val controller = session?.controller
        val metadata = controller?.metadata
        val playbackState = controller?.playbackState
        val actions = playbackState?.actions ?: 0L
        val canNext = actions and PlaybackState.ACTION_SKIP_TO_NEXT != 0L
        val canPrevious = actions and PlaybackState.ACTION_SKIP_TO_PREVIOUS != 0L
        val isPlaying = playbackState?.state == PlaybackState.STATE_PLAYING

        val builder = NotificationCompat.Builder(context, WRCNotifications.CHANNEL_MEDIA)
            .setContentTitle(metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "")
            .setContentText(metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "")
            .setLargeIcon(metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)

        val compactIndices = mutableListOf<Int>()
        var index = 0
        if (canPrevious) {
            builder.addAction(android.R.drawable.ic_media_previous, "Prev", actionIntent("PREV"))
            compactIndices += index++
        }
        builder.addAction(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
            if (isPlaying) "Pause" else "Play",
            actionIntent(if (isPlaying) "PAUSE" else "PLAY")
        )
        compactIndices += index++
        if (canNext) {
            builder.addAction(android.R.drawable.ic_media_next, "Next", actionIntent("NEXT"))
            compactIndices += index
        }

        val compatToken = session?.sessionToken?.let { MediaSessionCompat.Token.fromToken(it) }
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(compatToken)
                .setShowActionsInCompactView(*compactIndices.toIntArray())
        )

        return builder.build()
    }


    fun showMediaNotification(session: MediaSession?) {
        notificationManager.notify(WRCNotifications.MEDIA_ID, buildMediaNotification(session))
    }

    fun hideMediaNotification() {
        notificationManager.cancel(WRCNotifications.MEDIA_ID)
    }

    private fun actionIntent(action: String) = PendingIntent.getService(
        context, action.hashCode(),
        Intent(context, WRCService::class.java).setAction(action),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun buildConnectionNotification(state: ConnectionState): Notification {
        val text = when (state) {
            ConnectionState.Connected -> "Подключение к компьютеру активно"
            ConnectionState.Error -> "Соединение разорвано"
            else -> "Нет подключения..."
        }

        return NotificationCompat.Builder(context, WRCNotifications.CHANNEL_CONNECTION)
            .setContentTitle("WRC")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .apply {
                when (state) {
                    ConnectionState.Connected -> {
                        addAction(android.R.drawable.ic_menu_close_clear_cancel, "Отключить", actionIntent("DISCONNECT"))
                    }
                    ConnectionState.Error -> {
                        addAction(android.R.drawable.ic_menu_rotate, "Переподключить", actionIntent("RECONNECT"))
                        addAction(android.R.drawable.ic_menu_close_clear_cancel, "Выйти", actionIntent("EXIT"))
                    }
                    else -> {}
                }
            }
            .build()
    }

    fun updateConnectionNotification(state: ConnectionState) {
        notificationManager.notify(WRCNotifications.CONNECTION_ID, buildConnectionNotification(state))
    }


}