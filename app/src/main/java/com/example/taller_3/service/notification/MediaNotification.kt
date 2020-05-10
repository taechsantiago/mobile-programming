package com.example.taller_3.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.example.taller_3.AppConstants
import com.example.taller_3.MainActivity
import com.example.taller_3.R
import com.example.taller_3.service.MediaPlaybackService
import com.example.taller_3.service.library.MusicLibrary

class MediaNotification(private val service: MediaPlaybackService) {

    private val TAG="MediaNotification"

    private val playAction: NotificationCompat.Action
    private val pauseAction: NotificationCompat.Action
    private val nextAction: NotificationCompat.Action
    private val prevAction: NotificationCompat.Action
    val mNotificationManager: NotificationManager

    fun getNotification(
        context: Context,
        metadata: MediaMetadataCompat,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token
    ): Notification {
        val isPlaying = (state.state == PlaybackStateCompat.STATE_PLAYING)
        val description = metadata.description
        val builder= getNotificationBuilder(context,state, token, isPlaying, description)
        return builder.build()
    }
    fun getNotificationBuilder(
        context: Context,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token,
        playing: Boolean,
        description: MediaDescriptionCompat
    ): NotificationCompat.Builder {
        initNotificationChannels(service)
        val builder= NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_MAIN)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle(description.title)
            .setContentText(description.subtitle)
            .setLargeIcon(description.mediaId?.let { MusicLibrary.getAlbumBitmap(service, it) })

            .setContentIntent(createContentIntent())
            .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_STOP))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)


            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0,1,2)
                .setMediaSession(token)

                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_STOP))
            )
        if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS != 0L) {
            builder.addAction(prevAction)
        }
        builder.addAction(if (playing) pauseAction else playAction)
        if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_NEXT != 0L) {
            builder.addAction(nextAction)
        }
        return builder
    }

    fun getNotificationManager(): NotificationManager {
        return mNotificationManager
    }

    private fun createContentIntent(): PendingIntent? {
        val openAppIntent = Intent(service, MainActivity::class.java)
        openAppIntent.putExtra(AppConstants.EXTRA_FRAGMENT,"TracksDescriptionFragment")
        openAppIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent= PendingIntent.getActivity(
            service,
            REQUEST_CODE,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }


    private fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                context,
                NOTIFICATION_CHANNEL_MAIN,
                "MAIN CHANNEL",
                NotificationManager.IMPORTANCE_DEFAULT,
                "All notifications use this channel"
            )
        }
    }

    private fun createNotificationChannel(
        context: Context,
        id: String,
        name: CharSequence,
        importance: Int,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(id, name, importance)
            notificationChannel.description = description
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    init {
        mNotificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        playAction= NotificationCompat.Action(
            R.drawable.song_play_notification,
            "play",
            MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PLAY))
        Log.d(TAG,"PendingIntent play: ${MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PLAY)}")

        pauseAction= NotificationCompat.Action(
            R.drawable.song_pause_notification,
            "pause",
            MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PAUSE))
        Log.d(TAG,"PendingIntent pause: ${MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PAUSE)}")

        nextAction= NotificationCompat.Action(
            R.drawable.song_next_notification,
            "next",
            MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_NEXT))
        Log.d(TAG,"PendingIntent next: ${MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)}")

        prevAction= NotificationCompat.Action(
            R.drawable.song_previus_notification,
            "past",
            MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS))
        Log.d(TAG,"PendingIntent past: ${MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)}")

        mNotificationManager.cancelAll()
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_MAIN = "NOTIFICATION_CHANNEL_MAIN"
        private const val REQUEST_CODE=100
        const val NOTIFICATION_ID = 200
    }
}