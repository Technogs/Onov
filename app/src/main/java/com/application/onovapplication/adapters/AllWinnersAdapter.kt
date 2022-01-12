package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvAllWinnersBinding
import com.application.onovapplication.model.AllWinnersList
import com.bumptech.glide.Glide
import com.application.onovapplication.repository.BaseUrl

class AllWinnersAdapter(val context: Context, val allWinnersList: ArrayList<AllWinnersList>) :
    RecyclerView.Adapter<AllWinnersAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvAllWinnersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
//        return RVHolder(LayoutInflater.from(context).inflate(R.layout.rv_all_winners, parent, false)
        //)
    }

    override fun getItemCount(): Int {
        return allWinnersList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.bind(allWinnersList[position])
    }


    inner class RVHolder(val binding: RvAllWinnersBinding) :
//    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(allWinnersList: AllWinnersList) {

            Glide.with(context).load( BaseUrl.photoUrl+allWinnersList.profilePic).into(
            binding.winnerImage)

            binding.tvWinnerName.text = allWinnersList.fullName
            binding.tvWinnerRank.text = "#".plus(allWinnersList.rank)


        }

    }

}
