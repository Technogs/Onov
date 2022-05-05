package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.activities.common.ProfileActivity2
import com.application.onovapplication.databinding.RvAllWinnersBinding
import com.application.onovapplication.model.AllWinnersList
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide

class AllWinnersAdapter(val context: Context, val allWinnersList: ArrayList<AllWinnersList>) :
    RecyclerView.Adapter<AllWinnersAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding =
            RvAllWinnersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)

    }

    override fun getItemCount(): Int {
        return allWinnersList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.bind(allWinnersList[position])
        holder.binding.allWinnerLyt.setOnClickListener {

            val intent = Intent(context, ProfileActivity2::class.java)
            intent.putExtra("type", "other")
            intent.putExtra("usrRef", allWinnersList[position].winnerRef)
            context.startActivity(intent)
        }

    }


    inner class RVHolder(val binding: RvAllWinnersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(allWinnersList: AllWinnersList) {

            Glide.with(context).load(BaseUrl.photoUrl + allWinnersList.profilePic)
                .into(binding.winnerImage)
            binding.tvWinnerName.text = allWinnersList.fullName
            binding.tvWinnerRank.text =
                "#".plus(allWinnersList.rank) + " (" + allWinnersList.winCount + " Votes)"

        }

    }

}
