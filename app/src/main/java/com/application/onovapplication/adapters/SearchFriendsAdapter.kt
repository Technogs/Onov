package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.ProfileActivity2
import com.application.onovapplication.databinding.ReplyRowBinding
import com.application.onovapplication.databinding.RvPredefinedTextsBinding
import com.application.onovapplication.databinding.RvSearchFriendsBinding
import com.application.onovapplication.model.SearchData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SearchFriendsAdapter(val context: Context, var list: List<SearchData>,val onviewClick: OnViewClick) : RecyclerView.Adapter<SearchFriendsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvSearchFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(list[position])

       holder.binding.ivSearchProfile.setOnClickListener {
           val intent = Intent(context, ProfileActivity2::class.java)
           intent.putExtra("type", "other")
           intent.putExtra("usrRef", list[position].userRef)
           context.startActivity(intent)
       }
        holder.binding.follow.setOnClickListener { onviewClick.onClick(list[position]) }

    }


    inner class RVHolder(val binding: RvSearchFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {
fun bind(searchData: SearchData){
    binding.chatUserName.text= searchData.fullName
Glide.with(context).load(BaseUrl.photoUrl + searchData.profilePic).apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(binding.ivProfilepic)

    if (searchData.follow=="0") {binding.follow.text="Follow"}
      else  {binding.follow.text="UnFollow"}

}
    }


    interface OnViewClick{
        fun onClick(dataItem: SearchData)
    }
}
