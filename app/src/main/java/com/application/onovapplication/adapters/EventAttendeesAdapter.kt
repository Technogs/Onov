package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvEventAttendeesBinding
import com.application.onovapplication.model.AttendeeData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions



class EventAttendeesAdapter (val context: Context,val attendees:List<AttendeeData>) : RecyclerView.Adapter<EventAttendeesAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvEventAttendeesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return attendees.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(attendees[position])


    }


    inner class RVHolder(val binding: RvEventAttendeesBinding) : RecyclerView.ViewHolder(binding.root) {
fun bind(dataItem:AttendeeData){
    Glide.with(context).load(BaseUrl.photoUrl +dataItem.profilePic).apply(RequestOptions().placeholder(R.drawable.dummy_profile_image)).into(binding.ivFollowProfile)
    binding.followUsername.text=dataItem.fullName
}

    }

}
