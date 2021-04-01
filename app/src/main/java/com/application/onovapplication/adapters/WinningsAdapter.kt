package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.model.StatsDataList
import kotlinx.android.synthetic.main.rv_stats.view.*

class WinningsAdapter(
    val context: Context,
    private val winningsList: ArrayList<StatsDataList>
) : RecyclerView.Adapter<WinningsAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_stats, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return winningsList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.bind(winningsList[position])
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(statsDataList: StatsDataList) {
            itemView.tvStatsTitle.text = statsDataList.title
            itemView.tvStatsVotes.text = statsDataList.createdAt
        }

    }

}
