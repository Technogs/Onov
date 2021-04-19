package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.DebateDetailsActivity
import kotlinx.android.synthetic.main.rv_debate_requests.view.*

class DebateRequestsAdapter(
    val context: Context
) : RecyclerView.Adapter<DebateRequestsAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_debate_requests, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.itemView.btnView.setOnClickListener {
            val intent = Intent(context, DebateDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

}
