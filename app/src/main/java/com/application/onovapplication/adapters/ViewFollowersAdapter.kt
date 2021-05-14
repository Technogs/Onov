package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.ProfileActivity2
import kotlinx.android.synthetic.main.rv_followers.view.*

class ViewFollowersAdapter(
    val context: Context, val type: String
) : RecyclerView.Adapter<ViewFollowersAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_followers, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        when (type) {
            "followers" -> {
                holder.itemView.btnText.text = context.getString(R.string.remove)
            }
            "following" -> {
                holder.itemView.btnText.text = context.getString(R.string.unfollow)
            }
            else -> {
                holder.itemView.btnText.visibility = View.GONE

            }
        }


        holder.itemView.ivFollowProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity2::class.java)
            intent.putExtra("type", "other")
            context.startActivity(intent)
        }
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
