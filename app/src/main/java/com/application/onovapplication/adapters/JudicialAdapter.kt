package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R

class JudicialAdapter(var context: Context) : RecyclerView.Adapter<JudicialAdapter.JudicialViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JudicialViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view_judicial, parent, false)
        return JudicialViewHolder(v)
    }

    override fun onBindViewHolder(holder: JudicialViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return 12
    }

    class JudicialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}