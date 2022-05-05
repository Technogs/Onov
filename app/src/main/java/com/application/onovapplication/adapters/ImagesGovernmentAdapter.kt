package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ItemViewGovernmentImagesBinding
import com.application.onovapplication.model.GovtData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ImagesGovernmentAdapter(var context: Context,var members:List<GovtData>,val adapterOnClick : OnAdapterClick) : RecyclerView.Adapter<ImagesGovernmentAdapter.ImagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = ItemViewGovernmentImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
holder.bind(members[position])

        Glide.with(context).load(BaseUrl.photoUrl+ members[position].picture).apply(RequestOptions().
        placeholder(R.drawable.dummy_image)).into(holder.binding.userImage)
        holder.binding.imageLyt.setOnClickListener {
       adapterOnClick.doClick(members[position])
        }
    }
    override fun getItemCount(): Int {
        return members.size
    }

    class ImagesViewHolder(val binding: ItemViewGovernmentImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: GovtData?){
            binding.candidateName.text=dataItem?.name+"("+dataItem?.party+")"

        }
    }

    interface OnAdapterClick{
        fun doClick(dataItem: GovtData?)
    }
}