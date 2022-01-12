package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.ProfileActivity2
import com.application.onovapplication.databinding.RvChatsBinding
import com.application.onovapplication.databinding.RvFollowersBinding
import com.application.onovapplication.model.Follow
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ViewFollowersAdapter(val context: Context, val type: String, val frag: String, val onclick: OnPeopleClick,val follow:List<Follow>
) : RecyclerView.Adapter<ViewFollowersAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return follow.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
holder.bind(follow[position])
        when (type) {
            "followers" -> {
                holder.binding.btnText.text = context.getString(R.string.remove)
            }
            "following" -> {
                holder.binding.btnText.text = context.getString(R.string.unfollow)
            } "people" -> {
                holder.binding.btnText.visibility=View.GONE
            holder.binding.rltLyt.setOnClickListener {
                onclick.onPClick(follow[position])
            }
            }
            else -> {
                holder.binding.btnText.visibility = View.GONE

            }
        }


        holder.binding.ivFollowProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity2::class.java)
            intent.putExtra("type", "other")
            context.startActivity(intent)
        }
    }


    inner class RVHolder(val binding: RvFollowersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: Follow) {
            if (frag=="donation" || frag=="debate" )binding.checkbox.visibility=View.VISIBLE
            else binding.checkbox.visibility=View.GONE

            binding.followUsername.text=dataItem.fullName
        Glide.with(context).load(BaseUrl.photoUrl +dataItem.profilePic).apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(binding.ivFollowProfile)

   binding.checkbox.setOnClickListener { onclick.onCheckboxClick(dataItem) }
    }
    }
interface OnPeopleClick{
    fun onPClick(datatem:Follow)
    fun onCheckboxClick(datatem:Follow)

}

}
