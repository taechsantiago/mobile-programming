package com.audia.taller_3.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SongChangeBroadcastReceiver(private val songChangeDuringPlay: SongChangeDuringPlay) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            songChangeDuringPlay.changeSong(intent)
        }
    }
}