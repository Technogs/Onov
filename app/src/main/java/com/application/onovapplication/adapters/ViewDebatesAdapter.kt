package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.model.MoreScreenData
import kotlinx.android.synthetic.main.rv_debates.view.*
import kotlinx.android.synthetic.main.rv_more.view.*

class ViewDebatesAdapter(
    val context: Context
) : RecyclerView.Adapter<ViewDebatesAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_debates, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        if (position == 3 || position == 1)
        {
            holder.itemView.ivFeed.visibility = View.GONE
        }
        else{
            holder.itemView.ivFeed.visibility = View.VISIBLE

        }
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
