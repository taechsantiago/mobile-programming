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
import com.example.taller_3.Adapters.TracksListAdapter
import com.example.taller_3.DataBase.Tracks
import com.example.taller_3.DataBase.TracksRoomDatabase
import com.example.taller_3.DataBase.TracksViewModel
import com.example.taller_3.OnItemClickListener
import com.example.taller_3.R
import kotlinx.android.synthetic.main.album_list.view.*
import kotlinx.android.synthetic.main.item_track.view.*

class AlbumList: Fragment() {
    private lateinit var mView: View

    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var album_list_recycler:RecyclerView
    private lateinit var album_name:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.album_list, container, false)
        tracksViewModel= activity?.run{ViewModelProvider(this).get(TracksViewModel::class.java)
            }?: throw Exception("Invalid activity")

        arguments?.apply {
            album_name=getString("Album") ?: ""
        }

        addObserver()

        return mView
    }

    private fun addObserver() {
        val adapter = context?.let { TracksListAdapter(it) }
        album_list_recycler=mView.findViewById(R.id.album_list_recycler)
        album_list_recycler.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        album_list_recycler.adapter = adapter

        when (album_name) {
            "AM" -> {
                tracksViewModel.amTrackList.observe(viewLifecycleOwner, Observer { tracksList ->
                    tracksList?.let {
                        if (adapter != null) {
                            adapter.setTracks(it)
                        }
                    }
                })
            }
            "Colores" -> {
                tracksViewModel.coloresTrackList.observe(viewLifecycleOwner, Observer { tracksList ->
                    tracksList?.let {
                        if (adapter != null) {
                            adapter.setTracks(it)
                        }
                    }
                })
            }
            "YHLQMDLG" -> {
                tracksViewModel.yhlqmdlgTrackList.observe(viewLifecycleOwner, Observer { tracksList ->
                    tracksList?.let {
                        if (adapter != null) {
                            adapter.setTracks(it)
                        }
                    }
                })
            }
            else -> {
                tracksViewModel.quepasaTrackList.observe(viewLifecycleOwner, Observer { tracksList ->
                    tracksList?.let {
                        if (adapter != null) {
                            adapter.setTracks(it)
                        }
                    }
                })
            }
        }

    }



}






