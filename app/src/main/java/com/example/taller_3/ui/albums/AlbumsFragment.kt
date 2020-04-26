package com.example.taller_3.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.R
import kotlinx.android.synthetic.main.fragment_albums.view.*
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumsFragment : Fragment() {

    private lateinit var mView: View

    val data = arrayListOf(
        AlbumsViewModel(R.drawable.am, "Arctic Monkeys", "AM"),
        AlbumsViewModel(R.drawable.colores, "J Balvin", "Colores"),
        AlbumsViewModel(R.drawable.yh, "Bad Bunny","YHLQMDLG" ),
        AlbumsViewModel(R.drawable.quepasa, "La Toma","¿Que pasa en casa?")
    )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_albums, container, false)


        mView.recyclerView2.layoutManager= GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mView.recyclerView2.adapter=AlbumsAdapter(data)

        return mView
    }

    private class AlbumsAdapter(val AlbumsList:ArrayList<AlbumsViewModel>) :
        RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)

            return AlbumViewHolder(
                itemView,
                itemView.album_item_image,
                itemView.album_item_text,
                itemView.album_item_text1
            )
        }

        override fun getItemCount(): Int = AlbumsList.size

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            val albums = AlbumsList[position]
            val icon= ContextCompat.getDrawable(holder.image.context,albums.album_image)
            holder.image.setImageDrawable(icon)
            holder.Txt.text = albums.album_artist
            holder.Txtt.text=albums.album_name
        }
        class AlbumViewHolder(val view: View, val image: ImageView, val Txt: TextView, val Txtt:TextView) :
            RecyclerView.ViewHolder(view)
    }

}


