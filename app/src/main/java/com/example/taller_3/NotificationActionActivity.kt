package com.example.taller_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NotificationActionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tracks)

        when(intent?.action){
            AppConstants.ACTION_NOTIFICATION_CONTENT -> {
                val action = intent?.action
                val extra = intent?.getStringExtra(AppConstants.EXTRA_PHRASE)
                val formattedText = "Action: $action \n $extra"

            }
        }
    }
}
