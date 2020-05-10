package com.example.taller_3.service

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.taller_3.AppConstants
import com.example.taller_3.broadcast.IndexBroadcastReceiver
import com.example.taller_3.broadcast.IndexMedia
import com.example.taller_3.service.adapter.MyMediaPlayerAdapter
import com.example.taller_3.service.adapter.MyPlayerAdapter
import com.example.taller_3.service.adapter.PlaybackInfoListener
import com.example.taller_3.service.library.MusicLibrary
import com.example.taller_3.service.notification.MediaNotification
import java.util.ArrayList

class MediaPlaybackService: MediaBrowserServiceCompat(),
    IndexMedia {

    private val MY_MEDIA_ROOT_ID = "media_root_id"
    private val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"
    private val TAG = "MediaPlaybackService"

    private var mediaSession: MediaSessionCompat? = null
    private var mPlayback: MyPlayerAdapter? = null
    private var mediaNotification: MediaNotification?=null
    private var callback: MediaSessionCallback?=null
    private var mServiceInStartedState = false
    private var repeatActive=false
    private var randomActive=false

    var startindexMedia=0


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"Oncreate, Service")
        applicationContext.registerReceiver(
            IndexBroadcastReceiver(
                this
            ),IntentFilter(AppConstants.ACTION_SERVICE_INDEX))

        callback=MediaSessionCallback()

        mediaSession= MediaSessionCompat(this@MediaPlaybackService,"MediaPlaybackService")
        mediaSession!!.setCallback(callback)
        mediaSession!!.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        sessionToken=mediaSession!!.sessionToken

        mediaNotification=
            MediaNotification(
                this
            )
        mPlayback =
            MyMediaPlayerAdapter(
                this@MediaPlaybackService,
                MediaPlayerListener()
            )

    }

    override fun startIndexMedia(intent: Intent) {
        startindexMedia=intent.getIntExtra("INDEX_MEDIA",0)
        callback?.setQueueIndex(startindexMedia)
        Log.d(TAG,"Indice recibido del fragmento: ${startindexMedia}, Service")
    }
    override fun onDestroy() {
        mPlayback?.stop()
        mediaSession!!.release()
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(MusicLibrary.getMediaItems())
    }
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return if (TextUtils.equals(clientPackageName, packageName)) {
            BrowserRoot(MY_MEDIA_ROOT_ID, null)
        } else {
            BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
        }
    }


    inner class MediaPlayerListener(): PlaybackInfoListener {
        private val mServiceManager = ServiceManager()

        override fun onStateChanged(state: PlaybackStateCompat) {
            mediaSession!!.setPlaybackState(state)
            when (state.state) {
                PlaybackStateCompat.STATE_PLAYING -> { mServiceManager.moveServiceToStartedState(state) }
                PlaybackStateCompat.STATE_PAUSED -> mServiceManager.updateNotificationForPause(state)
                PlaybackStateCompat.STATE_STOPPED -> mServiceManager.moveServiceOutOfStartedState(state)
            }
        }

        override fun onPlaybackCompleted() {

            Log.d("MediaPlaybackService","Enviando cambio de cancion, desde el servicio")
            val intent=Intent(AppConstants.ACTION_SERVICE_CHANGE_SONG).apply {
                putExtra("CHANGE_SONG", false)
            }
            applicationContext?.sendBroadcast(intent)

            if(!randomActive){
                Log.d(TAG,"Modo de reproduccion aleatoria desactivado")
                if(!repeatActive) {
                    Log.d(TAG,"Modo de reproduccion continua desactivado")
                    if ((callback?.getPlayListSize()?.minus(1)) != callback?.getQueueIndex()) {
                        mPlayback?.setNewState(PlaybackStateCompat.STATE_PLAYING)
                        callback?.onSkipToNext()
                    } else {
                        mPlayback?.setNewState(PlaybackStateCompat.STATE_PAUSED)
                    }
                }
                else{
                    Log.d(TAG,"Modo de reproduccion continua activado")
                    mPlayback?.setNewState(PlaybackStateCompat.STATE_PLAYING)
                    callback?.onSkipToNext()
                }
            }
            else{
                Log.d(TAG,"Modo de reproduccion aleatoria activado")
                mPlayback?.setNewState(PlaybackStateCompat.STATE_PLAYING)
                callback?.indexRandomPrevious(callback!!.getQueueIndex())
                callback?.randomIsActive()
            }
        }


        inner class ServiceManager{
            fun moveServiceToStartedState(state: PlaybackStateCompat) {
                val notification= mPlayback?.getCurrentMedia()?.let {
                    sessionToken?.let { it1 ->
                        mediaNotification?.getNotification(this@MediaPlaybackService,
                            it,state, it1) } }

                if (!mServiceInStartedState) {
                    val intent = Intent(this@MediaPlaybackService, MediaPlaybackService::class.java)
                    ContextCompat.startForegroundService(this@MediaPlaybackService, intent)
                    mServiceInStartedState = true
                    Log.d(TAG,"Debe iniciar el servicio foregraound")
                }
                startForeground(200, notification)
            }

            fun updateNotificationForPause(state: PlaybackStateCompat) {
                stopForeground(false)
                val notification= mPlayback?.getCurrentMedia()?.let {
                    sessionToken?.let { it1 ->
                        mediaNotification?.getNotification(applicationContext,
                            it,state, it1) } }
                mediaNotification?.getNotificationManager()?.notify(200,notification)
            }

            fun moveServiceOutOfStartedState(state: PlaybackStateCompat) {
                Log.d(TAG,"Se detiene el servicio")
                stopForeground(true)
                stopSelf()
                mServiceInStartedState=false
            }
        }
    }

    inner class MediaSessionCallback: MediaSessionCompat.Callback(){

        private val playList: MutableList<MediaSessionCompat.QueueItem> = ArrayList()
        private var mQueueIndex = -1
        private var mQueueIndexPrevious=0
        private var mediaMetadata: MediaMetadataCompat? = null

        override fun onAddQueueItem(description: MediaDescriptionCompat) {
            playList.add(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            if(randomActive||repeatActive) {
                val intent = Intent(AppConstants.ACTION_SERVICE_RANDOM_REPEAT).apply {
                    if (randomActive) {
                        putExtra("RANDOM_SONG", true)
                    }
                    if (repeatActive) {
                        putExtra("REPEAT_SONG", true)
                    }
                }
                applicationContext?.sendBroadcast(intent)
            }
            //debe estar el numero que se va a iniciar la reproducion
            mQueueIndex = if (mQueueIndex == -1) startindexMedia else mQueueIndex
            mediaSession!!.setQueue(playList)
        }
        override fun onRemoveQueueItem(description: MediaDescriptionCompat) {
            playList.remove(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            mQueueIndex = if (playList.isEmpty()) -1 else mQueueIndex
            mediaSession!!.setQueue(playList)
        }
        override fun onPrepare() {
            if (mQueueIndex < 0 && playList.isEmpty()) {
                return
            }
            val mediaId = playList[mQueueIndex].description.mediaId
            mediaMetadata = MusicLibrary.getMetadata(applicationContext, mediaId!!)
            mediaSession!!.setMetadata(mediaMetadata)
            if (!mediaSession!!.isActive) {
                mediaSession!!.isActive=true
            }
        }
        override fun onPlay() {
            if( !isReadyToPlay() ) {
                Log.d(TAG,"Archivos en playlist inexistentes")
                return
            }
            if(mediaMetadata==null){
                onPrepare()
            }

            mPlayback?.playFromMedia(mediaMetadata)
            Log.d(TAG,"reproduccion playFromMedia")
        }
        override fun onPause() {
            Log.d(TAG,"debe pausar")
            mPlayback?.pause()
        }
        override fun onStop() {
            Log.d(TAG,"debe poner stop")
            mPlayback?.stop()
            mediaSession!!.isActive=false
        }
        override fun onSkipToPrevious() {
            if(!randomActive) {
                Log.d(TAG, "debe cambiar cancion anterior")
                if (mQueueIndex > 0) {
                    mQueueIndex -= 1
                } else {
                    mQueueIndex = playList.size - 1
                }
                mediaMetadata = null
                onPlay()
            }
            else{
                mQueueIndex=mQueueIndexPrevious
                mediaMetadata = null
                onPlay()
            }
        }
        override fun onSkipToNext() {
            if(!randomActive) {
                Log.d(TAG, "debe cambiar cancion siguiente")
                mQueueIndexPrevious=mQueueIndex
                mQueueIndex = ++mQueueIndex % playList.size
                mediaMetadata = null
                onPlay()
            }
            else{
                mQueueIndexPrevious=mQueueIndex
                randomIsActive()
            }
        }
        override fun onCustomAction(action: String?, extras: Bundle?) {
            when (action) {
                "REPEAT" -> {
                    repeatActive=true
                    Log.e(TAG, "Custom action is ${action}")
                }
                "NOT_REPEAT" -> {
                    repeatActive=false
                    Log.e(TAG, "Custom action is ${action}")
                }
                "RANDOM" -> {
                    randomActive=true
                    Log.e(TAG, "Custom action is ${action}")
                }
                "NOT_RANDOM" -> {
                    randomActive=false
                    Log.e(TAG, "Custom action is ${action}")
                }
            }
        }

        private fun isReadyToPlay(): Boolean {
            return playList.isNotEmpty()
        }

        fun getQueueIndex():Int{
            return mQueueIndex
        }
        fun setQueueIndex(index: Int){
            mQueueIndex=index
        }
        fun getPlayListSize():Int{
            return playList.size
        }

        fun randomIsActive(){
            mQueueIndex=(0 until playList.size).random()
            onPrepare()
            onPlay()
        }
        fun indexRandomPrevious(index: Int){
            mQueueIndexPrevious=index
        }
    }

}