package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.ProfileActivity
import kotlinx.android.synthetic.main.rv_followers.view.*
import kotlinx.android.synthetic.main.rv_search_friends.view.*

class SearchFriendsAdapter(val context: Context
) : RecyclerView.Adapter<SearchFriendsAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_search_friends, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

       holder.itemView.ivSearchProfile.setOnClickListener {
           val intent = Intent(context, ProfileActivity::class.java)
           intent.putExtra("type", "other")
           context.startActivity(intent)
       }

    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
