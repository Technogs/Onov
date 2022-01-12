package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.PollingSubmitBinding

class PollingSubmitAdapter (val context: Context,val list: List<String>,val clickOption: onClickOption) : RecyclerView.Adapter<PollingSubmitAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): RVHolder {
        val binding = PollingSubmitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.binding.etPollTitle.setOnClickListener {
            holder.binding.etPollTitle.setBackgroundDrawable(context.getDrawable(R.drawable.live_action_button_bg_pressed))
            clickOption.onClickOptions(  holder.binding.etPollTitle.text.toString())
        }
holder.binding.etPollTitle.setText(list[position])
    }



    inner class RVHolder(val binding: PollingSubmitBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
    interface onClickOption{
        fun onClickOptions(option:String)
    }

}