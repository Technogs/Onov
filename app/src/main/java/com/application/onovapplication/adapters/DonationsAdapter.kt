package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R

class DonationsAdapter(
    val context: Context
) : RecyclerView.Adapter<DonationsAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_donations, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
