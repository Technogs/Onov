package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ItemViewGovernmentImagesBinding
import com.application.onovapplication.databinding.RvMoreBinding
import com.application.onovapplication.model.MoreScreenData


class MoreScreenAdapter(
    val context: Context,
    private val dataList: ArrayList<MoreScreenData>,
    private val moreItemListener: MoreItemListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<MoreScreenAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
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


    inner class RVHolder(val binding: RvMoreBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(dataItem: MoreScreenData) {
            binding.ivMore.setBackgroundResource(dataItem.image)
            binding.tvMore.text = dataItem.name



        }
    }

    interface MoreItemListener {
        fun onClick(position: Int)
    }

}