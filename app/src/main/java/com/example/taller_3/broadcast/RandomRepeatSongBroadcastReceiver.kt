package com.example.taller_3.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RandomRepeatSongBroadcastReceiver (private val randomRepeatSong: RandomRepeatSong) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            randomRepeatSong.randomRepeatSong(intent)
        }
    }

}