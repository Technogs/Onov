package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvEventAttendeesBinding
import com.application.onovapplication.model.AttendeeData
import com.application.onovapplication.model.JoinerData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DebateJoineeAdapter (val context: Context, val attendees:List<JoinerData>, val onvoteClick: onVoteClick, val page: String) : RecyclerView.Adapter<DebateJoineeAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvEventAttendeesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return attendees.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(attendees[position])
if (page=="dbdt"){
    holder.binding.btnText.visibility=View.GONE
    holder.binding.btnVote.visibility=View.GONE
}
holder.binding.btnVote.setOnClickListener {
    onvoteClick.voteBtnClick(attendees[position])
}
    }


    inner class RVHolder(val binding: RvEventAttendeesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: JoinerData){
            Glide.with(context).load(BaseUrl.photoUrl +dataItem.profilePic).apply(
                RequestOptions().placeholder(
                    R.drawable.dummy_profile_image)).into(binding.ivFollowProfile)
            binding.followUsername.text=dataItem.userName
            binding.btnText.visibility=View.GONE
            binding.btnVote.visibility=View.VISIBLE
        }


    }
    interface onVoteClick{
        fun voteBtnClick(joinerData: JoinerData)
    }
}