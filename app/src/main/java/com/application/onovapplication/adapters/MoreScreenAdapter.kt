package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.onovapplication.R
import com.application.onovapplication.model.MoreScreenData
import kotlinx.android.synthetic.main.rv_more.view.*


class MoreScreenAdapter(
    val context: Context,
    private val dataList: ArrayList<MoreScreenData>,
    private val moreItemListener: MoreItemListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<MoreScreenAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_more, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(dataList[position])

        holder.itemView.setOnClickListener {
            moreItemListener.onClick(position)

        }
    }


    inner class RVHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(dataItem: MoreScreenData) {
            itemView.ivMore.setBackgroundResource(dataItem.image)
            itemView.tvMore.text = dataItem.name



        }
    }

    interface MoreItemListener {
        fun onClick(position: Int)
    }

}