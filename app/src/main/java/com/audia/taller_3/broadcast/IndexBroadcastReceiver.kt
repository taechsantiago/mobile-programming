package com.audia.taller_3.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class IndexBroadcastReceiver(private val indexMedia: IndexMedia) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            indexMedia.startIndexMedia(intent)
        }
    }

}