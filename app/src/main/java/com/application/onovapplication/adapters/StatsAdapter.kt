package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvSearchFriendsBinding
import com.application.onovapplication.databinding.RvStatsBinding
import com.application.onovapplication.model.StatsDataList

class StatsAdapter(val context: Context, private val statsList: List<StatsDataList>)
    : RecyclerView.Adapter<StatsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return statsList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.bind(statsList[position])
    }


    inner class RVHolder(val binding: RvStatsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(statsDataList: StatsDataList) {

            binding.tvStatsTitle.text = statsDataList.title
            binding.tvStatsVotes.text = statsDataList.voteCount
           // itemView.tvStatsViews.text = statsDataList.
        }

    }
}