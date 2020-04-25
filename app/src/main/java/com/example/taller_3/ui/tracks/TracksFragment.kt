package com.example.taller_3.ui.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.Adapters.TracksListAdapter
import com.example.taller_3.DataBase.TracksViewModel
import com.example.taller_3.R

class TracksFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var tracksViewModel: TracksViewModel
    private lateinit var tracks_list_view: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_tracks, container, false)

        tracksViewModel = activity?.run {
            ViewModelProvider(this).get(TracksViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        addObserver()

        return mView
    }

    private fun addObserver() {
        val adapter = context?.let { TracksListAdapter(it) }
        tracks_list_view=mView.findViewById(R.id.track_list)
        tracks_list_view.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        tracks_list_view.adapter = adapter
        tracksViewModel.tracksList.observe(viewLifecycleOwner, Observer { tracksList ->
            tracksList?.let {
                if (adapter != null) {
                    adapter.setTracks(it)
                }
            }
        })
    }
}
