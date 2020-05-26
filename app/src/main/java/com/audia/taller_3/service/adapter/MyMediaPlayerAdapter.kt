package com.audia.taller_3.service.adapter

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.audia.taller_3.AppConstants
import com.audia.taller_3.R
import com.audia.taller_3.service.library.MusicLibrary
import com.google.firebase.storage.FirebaseStorage

class MyMediaPlayerAdapter(private val context: Context, private val listener: PlaybackInfoListener): MyPlayerAdapter(context) {

    private val CUSTOM_ACTION_REPEAT = "REPEAT"
    private val TAG = "MyMediaPlayerAdapter"
    private val mContext: Context =context.applicationContext
    private val mPlaybackInfoListener: PlaybackInfoListener = listener

    private var mMediaPlayer: MediaPlayer? = null
    private var mFilename: String? = null
    private var currentMedia: MediaMetadataCompat? = null
    private var mCurrentMediaPlayedToCompletion = false

    private var mState = 0
    private var mSeekWhileNotPlaying = -1

    private val storage= FirebaseStorage.getInstance()


    override fun playFromMedia(metadata: MediaMetadataCompat?) {
        currentMedia = metadata
        val mediaId = metadata?.description?.mediaId
        Log.d(TAG,"Archivo a reproducir: ${MusicLibrary.getMusicFilename(mediaId)}")
        playFile(MusicLibrary.getMusicFilename(mediaId),mediaId)
    }
    override fun getCurrentMedia(): MediaMetadataCompat? {
        return currentMedia
    }
    override fun onPlay() {
        if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
            setNewState(PlaybackStateCompat.STATE_PLAYING)
        }
    }
    override fun onPause() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
            setNewState(PlaybackStateCompat.STATE_PAUSED)
        }
    }
    override fun onStop() {
        setNewState(PlaybackStateCompat.STATE_STOPPED)
        release()
    }
    override fun seekTo(position: Int) {
        if (mMediaPlayer != null) {
            if (!mMediaPlayer!!.isPlaying) {
                mSeekWhileNotPlaying = position.toInt()
            }
            mMediaPlayer!!.seekTo(position.toInt())
            setNewState(mState)
        }
    }
    override fun setVolume(volume: Float) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.setVolume(volume, volume)
        }
    }
    override fun isPlaying(): Boolean {
        val isplaying=((mMediaPlayer != null) && (mMediaPlayer!!.isPlaying))
        return isplaying
    }

    private fun initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setOnCompletionListener {
                setNewState(PlaybackStateCompat.STATE_PAUSED)
                mPlaybackInfoListener.onPlaybackCompleted()
            }
        }
    }
    override fun setNewState(newPlayerState: Int) {
        mState = newPlayerState

        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true
        }
        val reportPosition: Long

        if (mSeekWhileNotPlaying >= 0) {
            reportPosition = mSeekWhileNotPlaying.toLong()
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                mSeekWhileNotPlaying = -1
            }
        }
        else {
            if (mMediaPlayer == null){
                reportPosition=0
            }
            else{
                reportPosition=mMediaPlayer!!.currentPosition.toLong()
            }
        }

        val stateBuilder = PlaybackStateCompat.Builder()
        stateBuilder.setActions(availableActions())
        stateBuilder.addCustomAction(
            PlaybackStateCompat.CustomAction.Builder(AppConstants.CUSTOM_ACTION_REPEAT,context.resources.getString(
                R.string.repeat),mContext.resources.getIdentifier("repeat_song","drawable",mContext.packageName))
                .build()
        )
        stateBuilder.addCustomAction(
            PlaybackStateCompat.CustomAction.Builder(AppConstants.CUSTOM_ACTION_RANDOM,context.resources.getString(
                R.string.random),mContext.resources.getIdentifier("random_song","drawable",mContext.packageName))
                .build()
        )
        stateBuilder.setState(
            mState,
            reportPosition,
            1.0f,
            SystemClock.elapsedRealtime()
        )
        mPlaybackInfoListener.onStateChanged(stateBuilder.build())
    }
    private fun availableActions(): Long {
        var actions = (PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        when (mState) {
            PlaybackStateCompat.STATE_STOPPED -> actions = actions or
                    PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat . ACTION_PAUSE
            PlaybackStateCompat.STATE_PLAYING -> actions = actions or
                    (PlaybackStateCompat.ACTION_STOP
                            or PlaybackStateCompat.ACTION_PAUSE
                            or PlaybackStateCompat.ACTION_SEEK_TO)
            PlaybackStateCompat.STATE_PAUSED -> actions = actions or
                    PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat . ACTION_STOP
            PlaybackStateCompat.STATE_SKIPPING_TO_NEXT-> actions= actions or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            else -> actions = actions or
                    (PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_STOP
                            or PlaybackStateCompat.ACTION_PAUSE)
        }
        return actions
    }
    private fun playFile(musicFilename: String?, mediaIdFile: String?) {
        var mediaChanged = mFilename == null || musicFilename != mFilename
        if (mCurrentMediaPlayedToCompletion) {
            mediaChanged = true
            mCurrentMediaPlayedToCompletion = false
        }
        if (!mediaChanged) {
            if (!isPlaying()) {
                play()
            }
            return
        } else {
            release()
        }
        mFilename = musicFilename
        initializeMediaPlayer()

        val pathReference = musicFilename?.let { storage.reference.child("tracks").child(it) }
        pathReference?.downloadUrl?.addOnSuccessListener {
            val urL=it.toString()
            mMediaPlayer?.apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(context,it)
            }
            mMediaPlayer!!.prepare()
            play()
        }?.addOnFailureListener {
            Log.d(TAG,"no fue posible")
        }

        /*try {
            //se abre el archivo de musica para lectura, nos permite ver su longitud
            val cancionId = mContext.resources.getIdentifier(mediaIdFile, "raw", mContext.packageName)
            val assetFileDescriptor = mContext.resources.openRawResourceFd(cancionId)
            mMediaPlayer!!.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )

        } catch (e: Exception) {
            throw RuntimeException("Error al abrir el archivo: $mFilename", e)
        }
        try {
            mMediaPlayer!!.prepare()
        } catch (e: Exception) {
            throw RuntimeException("Error al abrir el archivo: $mFilename", e)
        }
        play()*/
    }
    private fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

}