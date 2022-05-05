package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R

class VotingAdapter(val context: Context
) : RecyclerView.Adapter<VotingAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_voting_item, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {}

}
