package com.audia.taller_3.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.audia.taller_3.AppConstants
import com.audia.taller_3.DataBase.Tracks
import com.audia.taller_3.R
import kotlinx.android.synthetic.main.item_track.view.*


class TracksListAdapter internal constructor(private val context: Context) : RecyclerView.Adapter<TracksListAdapter.ViewHolder>() {

    var tracksList = emptyList<Tracks>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

            val intent=Intent(AppConstants.ACTION_SERVICE_INDEX).apply {
                putExtra("INDEX_MEDIA", track.code)
            }
            context.applicationContext?.sendBroadcast(intent)

            val bundle = bundleOf("song_change_playing" to true,"track_code" to track.code)
            holder.view.findNavController().navigate(R.id.action_track_to_track_description, bundle)
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