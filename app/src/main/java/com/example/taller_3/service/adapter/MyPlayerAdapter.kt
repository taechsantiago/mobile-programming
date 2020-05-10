package com.example.taller_3.service.adapter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.support.v4.media.MediaMetadataCompat
import android.util.Log

abstract class MyPlayerAdapter(private val context: Context) {

    private val TAG="MyPlayerAdapter"

    private var noisyReceiver: broadcastReceiver

    private val mApplicationContext: Context = context.applicationContext
    private val mAudioManager: AudioManager
    private val mAudioFocusHelper: AudioFocusHelper

    private var mPlayOnAudioFocus = false
    private var mAudioNoisyReceiverRegistered = false

    abstract fun playFromMedia(metadata: MediaMetadataCompat?)
    abstract fun getCurrentMedia(): MediaMetadataCompat?
    abstract fun onPlay()
    abstract fun onPause()
    abstract fun onStop()
    abstract fun seekTo(position: Int)
    abstract fun setVolume(volume: Float)
    abstract fun isPlaying(): Boolean
    abstract fun setNewState(newPlayerState: Int)

    fun play() {
        if (mAudioFocusHelper.requestAudioFocus()) {
            registerAudioNoisyReceiver()
            onPlay()
        }
    }
    fun pause() {
        if (!mPlayOnAudioFocus) {
            mAudioFocusHelper.abandonAudioFocus()
        }
        unregisterAudioNoisyReceiver()
        onPause()
    }
    fun stop() {
        mAudioFocusHelper.abandonAudioFocus()
        unregisterAudioNoisyReceiver()
        onStop()
    }

    private fun registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            mApplicationContext.registerReceiver(
                noisyReceiver,
                AUDIO_NOISY_INTENT_FILTER
            )
            mAudioNoisyReceiverRegistered = true
        }
    }
    private fun unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            mApplicationContext.unregisterReceiver(noisyReceiver)
            mAudioNoisyReceiverRegistered = false
        }
    }

    inner class broadcastReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                Log.d(TAG,"Broadcast")
                if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                    if(isPlaying()){
                        pause()
                    }
                }
            }
        }

    }
    inner class AudioFocusHelper: AudioManager.OnAudioFocusChangeListener{
        override fun onAudioFocusChange(focusChange: Int) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    if (mPlayOnAudioFocus && !isPlaying()) {
                        play()
                    }
                    else if(isPlaying()){
                        setVolume(MEDIA_VOLUME_DEFAULT)
                    }
                    mPlayOnAudioFocus = false
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    setVolume(MEDIA_VOLUME_DUCK)
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    if(isPlaying()){
                        mPlayOnAudioFocus = false
                        pause()
                    }
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    if (isPlaying()) {
                        mAudioManager.abandonAudioFocus(this)
                        mPlayOnAudioFocus = false
                        stop()
                    }
                }
            }
        }

        fun requestAudioFocus(): Boolean {
            val answer=mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
            return answer == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }

        fun abandonAudioFocus() {
            mAudioManager.abandonAudioFocus(this)
        }
    }

    init {
        mAudioManager = mApplicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioFocusHelper = AudioFocusHelper()
        noisyReceiver=broadcastReceiver()
    }
    companion object {
        private const val MEDIA_VOLUME_DEFAULT = 1.0f
        private const val MEDIA_VOLUME_DUCK = 0.2f
        private val AUDIO_NOISY_INTENT_FILTER = IntentFilter(
            AudioManager.ACTION_AUDIO_BECOMING_NOISY
        )
    }

}