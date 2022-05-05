package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.PollingSubmitBinding

class PollingSubmitAdapter(
    val context: Context,
    val list: List<String>,
    val clickOption: onClickOption,
    val isMutiple: String
) : RecyclerView.Adapter<PollingSubmitAdapter.RVHolder>() {

    var opt: Boolean = false
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RVHolder {
        val binding =
            PollingSubmitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.binding.etPollTitle.setOnClickListener {
            if (isMutiple == "0" && opt == true) Toast.makeText(
                context,
                "you can't choose multiple options",
                Toast.LENGTH_SHORT
            ).show()
            else {
                opt = true
                holder.binding.etPollTitle.setBackgroundDrawable(context.getDrawable(R.drawable.live_action_button_bg_pressed))
                clickOption.onClickOptions(holder.binding.etPollTitle.text.toString())
            }
        }
        holder.binding.etPollTitle.text = list[position]
    }


    inner class RVHolder(val binding: PollingSubmitBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface onClickOption {
        fun onClickOptions(option: String)
    }

}