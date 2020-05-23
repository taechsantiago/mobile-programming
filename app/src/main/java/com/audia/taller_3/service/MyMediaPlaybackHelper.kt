package com.audia.taller_3.service

import android.content.ComponentName
import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat

abstract class MyMediaPlaybackHelper(private val context: Context, serviceClass:Class<MediaPlaybackService>) {

    private val mMediaBrowserServiceClass: Class<MediaPlaybackService>

    private val mMediaBrowserConnectionCallback: MediaBrowserConnectionCallback
    private val mMediaControllerCallback: MediaControllerCallback
    private val mMediaBrowserSubscriptionCallback: MediaBrowserSubscriptionCallback

    private val mCallbackList: MutableList<MediaControllerCompat.Callback> = ArrayList()
    private var mMediaBrowser: MediaBrowserCompat? = null
    private var mMediaController: MediaControllerCompat? = null

    fun onStart(){
        if (mMediaBrowser == null) {
            mMediaBrowser = MediaBrowserCompat(
                context,
                ComponentName(context, mMediaBrowserServiceClass),
                mMediaBrowserConnectionCallback,
                null
            )
            mMediaBrowser!!.connect()
        }
    }
    fun onStop(){
        if (mMediaController != null) {
            mMediaController!!.unregisterCallback(mMediaControllerCallback)
            mMediaController = null
        }
        if (mMediaBrowser != null && mMediaBrowser!!.isConnected) {
            mMediaBrowser!!.disconnect()
            mMediaBrowser = null
        }
        resetState()
    }

    //abstract fun onConnected(mMediaController: MediaControllerCompat)
    abstract fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>)
    private fun onDisconnected(){}

    fun getMediaController(): MediaControllerCompat {
        checkNotNull(mMediaController) { "MediaController fue creado" }
        return mMediaController as MediaControllerCompat
    }

    fun getTransportControls(): MediaControllerCompat.TransportControls{
        if (mMediaController == null) {
            throw IllegalStateException("MediaController no fue creado")
        }
        return mMediaController!!.transportControls
    }

    fun registerCallback(callback: MediaControllerCompat.Callback?) {
        if (callback != null) {
            mCallbackList.add(callback)
            if (mMediaController != null) {
                val metadata = mMediaController!!.metadata
                if (metadata != null) {
                    callback.onMetadataChanged(metadata)
                }
                val playbackState = mMediaController!!.playbackState
                if (playbackState != null) {
                    callback.onPlaybackStateChanged(playbackState)
                }
            }
        }
    }

    private fun resetState() {
        performOnAllCallbacks(object :CallbackCommand{
            override fun perform(callback: MediaControllerCompat.Callback?) {
                callback?.onPlaybackStateChanged(null)
            }
        })
    }
    private fun performOnAllCallbacks(command: CallbackCommand) {
        for (callback in mCallbackList) {
            if (callback != null) {
                command.perform(callback)
            }
        }
    }

    inner class MediaBrowserConnectionCallback: MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                mMediaController = MediaControllerCompat(context, mMediaBrowser!!.sessionToken)
                mMediaController!!.registerCallback(mMediaControllerCallback)
                mMediaControllerCallback.onMetadataChanged(mMediaController!!.metadata)
                mMediaControllerCallback.onPlaybackStateChanged(mMediaController!!.playbackState)
            } catch (e: RemoteException) {
                throw RuntimeException(e)
            }
            mMediaBrowser!!.subscribe(mMediaBrowser!!.root, mMediaBrowserSubscriptionCallback)
        }
    }

    inner class MediaControllerCallback: MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            performOnAllCallbacks(object : CallbackCommand{
                override fun perform(callback: MediaControllerCompat.Callback?) {
                    callback?.onMetadataChanged(metadata)
                }
            })
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            performOnAllCallbacks(object : CallbackCommand{
                override fun perform(callback: MediaControllerCompat.Callback?) {
                    callback?.onPlaybackStateChanged(state)
                }
            })
        }

        override fun onSessionDestroyed() {
            resetState()
            onPlaybackStateChanged(null)
            onDisconnected()
        }
    }

    inner class MediaBrowserSubscriptionCallback: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            this@MyMediaPlaybackHelper.onChildrenLoaded(parentId,children)
        }
    }

    private interface CallbackCommand {
        fun perform(callback: MediaControllerCompat.Callback?)
    }

    init {
        mMediaBrowserServiceClass=serviceClass
        mMediaBrowserConnectionCallback=MediaBrowserConnectionCallback()
        mMediaControllerCallback=MediaControllerCallback()
        mMediaBrowserSubscriptionCallback=MediaBrowserSubscriptionCallback()
    }
}