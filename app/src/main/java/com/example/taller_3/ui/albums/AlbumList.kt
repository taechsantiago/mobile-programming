package com.example.taller_3.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.DataBase.Tracks
import com.example.taller_3.DataBase.TracksRoomDatabase
import com.example.taller_3.DataBase.TracksViewModel
import com.example.taller_3.OnItemClickListener
import com.example.taller_3.R
import kotlinx.android.synthetic.main.album_list.view.*
import kotlinx.android.synthetic.main.item_track.view.*

class AlbumList: Fragment() {
    private lateinit var mView: View
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var tracksViewModel: TracksViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.album_list, container, false)
        tracksViewModel= activity?.run{ViewModelProvider(this).get(TracksViewModel::class.java)
            }?: throw Exception("Invalid activity")

        //addObserver()

        return mView
    }

    private fun loadAlbumList(album: String) {


        albumAdapter = AlbumAdapter()

        //albumAdapter.itemClickListener = context

        mView.album_list_recycler.adapter = albumAdapter
        mView.album_list_recycler.layoutManager = LinearLayoutManager(context)



    }



    private class AlbumAdapter() :
        RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

        var data: List<Tracks> = emptyList()
        var itemClickListener: OnItemClickListener?=null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)

            return AlbumViewHolder(
                itemView,
                itemView.track_imag,
                itemView.track_name_text,
                itemView.track_artist_text,
                itemView.track_duration_text
            )
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            val album = data[position]

            val icon= ContextCompat.getDrawable(holder.img.context,album.image)
            holder.img.setImageDrawable(icon)
            holder.nameTxt.text=album.name
            holder.artist.text=album.artist
            holder.duration.text=album.duration
            holder.view.setOnClickListener{
                itemClickListener?.onItemClick(it, holder.adapterPosition)
            }

            }
        class AlbumViewHolder(val view: View, val img: ImageView, val nameTxt: TextView, val artist:TextView ,val duration: TextView) :
            RecyclerView.ViewHolder(view)



        }


        }






