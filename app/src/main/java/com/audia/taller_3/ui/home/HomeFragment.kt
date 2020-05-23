package com.audia.taller_3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.audia.taller_3.Adapters.HomeAdapter
import com.audia.taller_3.R
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private lateinit var mView: View

     val data1 = arrayListOf(
        HomeView(R.drawable.ic_bad, "Bad Bunny"),
        HomeView(R.drawable.ic_j, "J Balvin"),
        HomeView(R.drawable.ic_arca, "La Toma")
    )


     val data2 = arrayListOf(
        HomeView(R.drawable.ic_arctic, "AM"),
        HomeView(R.drawable.ic_color, "Colores"),
        HomeView(R.drawable.ic_yh, "YHLQMDLG")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)

        mView.recyclerView.layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mView.recyclerView.adapter=context?.let { HomeAdapter(data1) }
        mView.recyclerView1.layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mView.recyclerView1.adapter=context?.let { HomeAdapter(data2) }

        return mView
    }

}


