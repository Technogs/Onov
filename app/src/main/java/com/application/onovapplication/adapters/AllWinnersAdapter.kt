package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.model.AllWinnersList
import com.bumptech.glide.Glide
import com.application.onovapplication.repository.BaseUrl
import kotlinx.android.synthetic.main.rv_all_winners.view.*

class AllWinnersAdapter(
    val context: Context,
    val allWinnersList: ArrayList<AllWinnersList>
) : RecyclerView.Adapter<AllWinnersAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_all_winners, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

//        holder.bind(allWinnersList[position])
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(allWinnersList: AllWinnersList) {

            Glide.with(context).load( BaseUrl.photoUrl+allWinnersList.profilePic).into(
            itemView.winnerImage)

            itemView.tvWinnerName.text = allWinnersList.fullName
            itemView.tvWinnerRank.text = "#".plus(allWinnersList.rank)


        }

    }

}
