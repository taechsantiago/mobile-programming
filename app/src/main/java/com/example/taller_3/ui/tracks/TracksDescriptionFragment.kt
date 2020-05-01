package com.example.taller_3.ui.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taller_3.DataBase.TracksViewModel
import com.example.taller_3.R
import kotlin.properties.Delegates

class TracksDescriptionFragment: Fragment() {

    private lateinit var mView: View

    private lateinit var tracksViewModel: TracksViewModel

    private var track_code by Delegates.notNull<Int>()
    private lateinit var image_track: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_tracks_description, container, false)

        tracksViewModel = activity?.run {
            ViewModelProvider(this).get(TracksViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        arguments?.apply {
            track_code=getInt("track_code")
        }

        val track=tracksViewModel.findByCode(track_code)

        mView.findViewById<TextView>(R.id.track_album_text_description).text=track.album

        image_track=mView.findViewById(R.id.track_album_img_description)
        image_track.setImageResource(track.image)

        mView.findViewById<TextView>(R.id.track_title_text_description).text=track.name
        mView.findViewById<TextView>(R.id.track_artist_text_description).text=track.artist

        return mView
    }
}