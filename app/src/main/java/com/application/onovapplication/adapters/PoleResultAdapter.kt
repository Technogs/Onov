package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.databinding.PollResultBinding
import com.application.onovapplication.model.PollResult

class PoleResultAdapter (val context: Context,val pollResult: List<PollResult>) : RecyclerView.Adapter<PoleResultAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = PollResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int { return pollResult.size }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
      //  holder.bind(pollResult[position])
        holder.binding.voteCount.setText( pollResult[position].valCount.toString())
        holder.binding.pollOption.setText(pollResult[position].name)
    }
    inner class RVHolder(val binding: PollResultBinding) :
        RecyclerView.ViewHolder(binding.root){

        }


    interface onclickCross {
        fun onclick()
        fun onClickOption(option: String)
    }

}
