package com.example.taller_3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_home.view.*


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
        mView.recyclerView.adapter=HomeAdapter(data1)
        mView.recyclerView1.layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mView.recyclerView1.adapter=HomeAdapter(data2)

        return mView
    }

    private class HomeAdapter(val HomeList:ArrayList<HomeView>) :
        RecyclerView.Adapter<HomeAdapter.ProductViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)

            return ProductViewHolder(
                itemView,
                itemView.text_item,
                itemView.image_item
            )
        }

        override fun getItemCount(): Int = HomeList.size

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val home = HomeList[position]
            val icon= ContextCompat.getDrawable(holder.image.context,home.image)
            holder.image.setImageDrawable(icon)
            holder.Txt.text = home.name
        }
        class ProductViewHolder(val view: View, val Txt: TextView, val image: ImageView) :
            RecyclerView.ViewHolder(view)
    }

        }


