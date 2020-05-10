package com.example.taller_3.service.adapter

import android.support.v4.media.session.PlaybackStateCompat

interface PlaybackInfoListener {

    fun onStateChanged(state: PlaybackStateCompat) {}
    fun onPlaybackCompleted() {}

}