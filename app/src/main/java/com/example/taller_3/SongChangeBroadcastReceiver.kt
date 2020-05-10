package com.example.taller_3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.taller_3.service.adapter.SongChangeDuringPlay

class SongChangeBroadcastReceiver(private val songChangeDuringPlay: SongChangeDuringPlay) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            songChangeDuringPlay.changeSong(intent)
        }
    }

}