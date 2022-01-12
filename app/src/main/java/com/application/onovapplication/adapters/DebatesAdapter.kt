package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.DebatesRowBinding
import com.application.onovapplication.databinding.RvAllWinnersBinding
import com.application.onovapplication.debate.vlive.ui.main.MainActivity
import com.application.onovapplication.model.AllWinnersList
import com.application.onovapplication.model.LiveDebate
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide

class DebatesAdapter (val context: Context,val type: String,val liveDebate: List<LiveDebate>,val onclickDebate: onClickDebate) : RecyclerView.Adapter<DebatesAdapter.RVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = DebatesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return liveDebate.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

//        Toast.makeText(context, type, Toast.LENGTH_SHORT).show()
       // Log.e("datahhhhh",liveDebate[position].message!!)
        holder.itemView.setOnClickListener {
            if (type=="live") {
//                Toast.makeText(context, type, Toast.LENGTH_SHORT).show()
    onclickDebate.onDebateItemClick(liveDebate[position])

            }
        }

        holder.bind(liveDebate[position])
    }


    inner class RVHolder(val binding: DebatesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(liveDebate: LiveDebate) {

            Glide.with(context).load( BaseUrl.photoUrl+liveDebate.coverImage).into(
                binding.debateCoverImage)

            binding.debateTopic.text = liveDebate.topic
            binding.debateTitle.text = liveDebate.title
            binding.debateVotes.text = liveDebate.vote
            binding.debateViews.text = liveDebate.view
            binding.debateDate.text = liveDebate.date
            binding.debateDesc.text = liveDebate.message
          //  binding.debateReq.text = liveDebate.re


        }

    }
    interface onClickDebate{
       fun onDebateItemClick(liveDebate: LiveDebate)
    }

}