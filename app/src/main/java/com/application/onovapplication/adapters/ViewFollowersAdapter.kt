package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.rv_followers.view.*

class ViewFollowersAdapter(val context: Context, val type: String
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

        if (type == "followers")
        {
            holder.itemView.btnText.text = context.getString(R.string.remove)
        }
        else{
            holder.itemView.btnText.text = context.getString(R.string.unfollow)

        }
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
