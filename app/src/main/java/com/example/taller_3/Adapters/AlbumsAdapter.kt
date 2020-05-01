package com.example.taller_3.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.R
import com.example.taller_3.ui.albums.AlbumsView
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumsAdapter internal constructor(private val albums: ArrayList<AlbumsView>): RecyclerView.Adapter<AlbumsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)

        return ViewHolder(
            itemView,
            itemView.album_item_image,
            itemView.album_item_text,
            itemView.album_item_text1
        )
    }

    override fun getItemCount(): Int = albums.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albums = albums[position]

        val icon = ContextCompat.getDrawable(holder.image.context, albums.album_image)
        holder.image.setImageDrawable(icon)
        holder.Txt.text = albums.album_artist
        holder.Txtt.text = albums.album_name

        holder.view.setOnClickListener {
            val bundle = bundleOf("Album" to albums.album_name)
            holder.view.findNavController().navigate(R.id.action_album_to_albumtracklist, bundle)
        }
    }
    inner class ViewHolder(
        val view: View,
        val image: ImageView,
        val Txt: TextView,
        val Txtt: TextView
    ) : RecyclerView.ViewHolder(view){
    }
}