package com.example.taller_3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.taller_3.service.adapter.IndexMedia

class DynamicBroadcastReceiver(private val indexMedia: IndexMedia) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            indexMedia.startIndexMedia(intent)
        }
    }

}