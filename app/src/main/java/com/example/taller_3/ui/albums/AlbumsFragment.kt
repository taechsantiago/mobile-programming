package com.example.taller_3.ui.albums

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.DataBase.TracksRoomDatabase
import com.example.taller_3.OnItemClickListener
import com.example.taller_3.R
import kotlinx.android.synthetic.main.fragment_albums.view.*
import kotlinx.android.synthetic.main.item_album.view.*


class AlbumsFragment : Fragment(), OnItemClickListener{

    private lateinit var mView: View


    val data = arrayListOf(
        AlbumsViewModel(R.drawable.am, "Arctic Monkeys", "AM"),
        AlbumsViewModel(R.drawable.colores, "J Balvin", "Colores"),
        AlbumsViewModel(R.drawable.yh, "Bad Bunny", "YHLQMDLG"),
        AlbumsViewModel(R.drawable.quepasa, "La Toma", "¿Que pasa en casa?")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_albums, container, false)

        mView.recyclerView2.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mView.recyclerView2.adapter = AlbumsAdapter(data,this)


        return mView
    }

    private class AlbumsAdapter(val AlbumsList: ArrayList<AlbumsViewModel>, val itemClickListener: OnItemClickListener?=null) :
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
            val icon = ContextCompat.getDrawable(holder.image.context, albums.album_image)
            holder.image.setImageDrawable(icon)
            holder.Txt.text = albums.album_artist
            holder.Txtt.text = albums.album_name

            holder.view.setOnClickListener {
                itemClickListener?.onItemClick(it, holder.adapterPosition)
            }
        }
        class AlbumViewHolder(
            val view: View,
            val image: ImageView,
            val Txt: TextView,
            val Txtt: TextView

            ) :

            RecyclerView.ViewHolder(view){

        }


    }

    override fun onItemClick(view: View, position: Int) {
        val navController = findNavController()
        val albums= data[position]
        val bundle = Bundle()
        bundle.putString("Album", albums.album_name)
        navController.navigate(R.id.navigation_albumlist, bundle)
    }
}


