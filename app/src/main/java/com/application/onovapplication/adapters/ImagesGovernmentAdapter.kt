package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R

class ImagesGovernmentAdapter(var context: Context) : RecyclerView.Adapter<ImagesGovernmentAdapter.ImagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view_government_images, parent, false)
        return ImagesViewHolder(v)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return 12
    }

    class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}