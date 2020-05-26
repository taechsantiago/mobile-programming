package com.audia.taller_3.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.audia.taller_3.R


class AlbumTrackListFragment: Fragment() {
    private lateinit var mView: View

    private lateinit var album_list_recycler:RecyclerView
    private lateinit var album_name:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_album_tracks_list, container, false)

        arguments?.apply {
            album_name=getString("Album") ?: ""
        }

        return mView
    }
}






