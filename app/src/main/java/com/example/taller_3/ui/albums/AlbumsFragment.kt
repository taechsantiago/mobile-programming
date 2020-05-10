package com.example.taller_3.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taller_3.Adapters.AlbumsAdapter
import com.example.taller_3.R
import kotlinx.android.synthetic.main.fragment_albums.view.*


class AlbumsFragment : Fragment(){

    private lateinit var mView: View

    val albums = arrayListOf(
        AlbumsView(R.drawable.album_am, "Arctic Monkeys", "AM"),
        AlbumsView(R.drawable.album_colores, "J Balvin", "Colores"),
        AlbumsView(R.drawable.album_yhlqmdlg, "Bad Bunny", "YHLQMDLG"),
        AlbumsView(R.drawable.album_la_toma, "La Toma", "¿Que pasa en casa?")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_albums, container, false)

        mView.recyclerView2.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mView.recyclerView2.adapter = AlbumsAdapter(albums)

        return mView
    }

}


