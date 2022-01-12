package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ReplyRowBinding
import com.application.onovapplication.databinding.RvPredefinedTextsBinding
import com.application.onovapplication.model.CommentData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ReplyAdapter (val context: Context, val cmntlist:List<CommentData>)
    : RecyclerView.Adapter<ReplyAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = ReplyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return cmntlist.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(cmntlist[position])

    }


    inner class RVHolder(val binding: ReplyRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataitem: CommentData){
            binding.tvComment.text=dataitem.comment
            binding.tvNameCmnt.text=dataitem.fullName
            Glide.with(context).load(BaseUrl.photoUrl + dataitem.profilePic).apply(
                RequestOptions().placeholder(
                    R.drawable.ic_baseline_account_circle_24)).into(binding.ivCommentProfile)

//            itemView.receivedFrom.text=dataitem.donateFrom

        }
    }
}