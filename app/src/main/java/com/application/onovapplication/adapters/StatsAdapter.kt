package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            if (statsDataList.myResult=="")
            binding.tvStatsViews.text = "NP"
            else   if (statsDataList.myResult=="0")
                binding.tvStatsViews.text = "L"
            else   if (statsDataList.myResult=="winner")
                binding.tvStatsViews.text = "W"
            else   if (statsDataList.myResult=="tie")
                binding.tvStatsViews.text = "T"
        }

    }
}