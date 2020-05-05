package com.example.taller_3.Adapters

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import android.service.notification.NotificationListenerService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.AppConstants
import com.example.taller_3.DataBase.Tracks
import com.example.taller_3.MainActivity
import com.example.taller_3.NotificationActionActivity
import com.example.taller_3.R
import kotlinx.android.synthetic.main.item_track.view.*

class TracksListAdapter internal constructor(private val context: Context) : RecyclerView.Adapter<TracksListAdapter.ViewHolder>() {

    private var tracksList = emptyList<Tracks>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        createNotificationChannel()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(
            view,
            view.track_imag,
            view.track_name_text,
            view.track_artist_text,
            view.track_duration_text
        )
    }

    override fun getItemCount() = tracksList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track=tracksList[position]

        val image= ContextCompat.getDrawable(holder.image.context,track.image)
        holder.image.setImageDrawable(image)

        holder.name.text=track.name
        holder.artist.text="${track.artist}"
        holder.duration.text="${track.duration}"

        holder.view.setOnClickListener {
            val bundle = bundleOf("track_code" to track.code)
            holder.view.findNavController().navigate(R.id.action_track_to_track_description, bundle)
            showCustomMessageNotification(holder.view)
        }

    }

    fun showCustomMessageNotification(view: View) {
        NotificationManagerCompat.from(context).notify(MainActivity.NOTIFICATION_ID_BASIC, showBasicNotification())
    }

    fun showBasicNotification() : Notification {

        val intentPrevious = Intent(AppConstants.ACTION_PREVIOUS, null, context, NotificationActionActivity::class.java)
        val intentPlay = Intent(AppConstants.ACTION_PLAY, null, context, NotificationActionActivity::class.java)
        val intentNext = Intent(AppConstants.ACTION_NEXT, null, context, NotificationActionActivity::class.java)

        val pendingIntentPrevious = PendingIntent.getActivity(context, 800, intentPrevious, 0)
        val pendingIntentPlay = PendingIntent.getActivity(context, 800, intentPlay, 0)
        val pendingIntentNext = PendingIntent.getActivity(context, 800, intentNext, 0)
        val Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_not)

        return NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_HIGH)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle("Song")
            .setContentText("Artist")
            .setLargeIcon(Bitmap)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_skip_previous, "Previous", pendingIntentPrevious)
            .addAction(R.drawable.ic_play, "Play", pendingIntentPlay)
            .addAction(R.drawable.ic_skip_next, "Next", pendingIntentNext)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2))
            .setSubText("Music Player")
            .build()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_HIGH,
                "High",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "High"

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    inner class ViewHolder(
        val view: View,
        val image: ImageView,
        val name: TextView,
        val artist: TextView,
        val duration: TextView
    ) : RecyclerView.ViewHolder(view) {
    }

    internal fun setTracks(track: List<Tracks>) {
        this.tracksList = track
        notifyDataSetChanged()
    }
}