package com.example.taller_3.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taller_3.R
import com.example.taller_3.ui.home.HomeView
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter internal constructor(private val HomeList:ArrayList<HomeView>): RecyclerView.Adapter<HomeAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)

        return ViewHolder(
            itemView,
            itemView.text_item,
            itemView.image_item
        )
    }

    override fun getItemCount(): Int = HomeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val home = HomeList[position]
        val icon= ContextCompat.getDrawable(holder.image.context,home.image)
        holder.image.setImageDrawable(icon)
        holder.Txt.text = home.name
    }
    inner class ViewHolder(
        val view: View,
        val Txt: TextView,
        val image: ImageView
    ) : RecyclerView.ViewHolder(view) {
    }
}