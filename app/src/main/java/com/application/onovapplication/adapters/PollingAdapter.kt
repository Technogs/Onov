package com.application.onovapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.databinding.RvPollingBinding
import com.application.onovapplication.model.PollingOptions

class PollingAdapter(
    val context: Context, val onClickCross: onclickCross, val plo: PollingOptions
) : RecyclerView.Adapter<PollingAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RVHolder {
        val binding = RvPollingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return plo.count
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.binding.crossIcon.setOnClickListener {
            onClickCross.onclick()
        }

        holder.binding.etPollOption.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) else onClickCross.onClickOption(holder.binding.etPollOption.text.toString())
        }

    }


    inner class RVHolder(val binding: RvPollingBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface onclickCross {
        fun onclick()
        fun onClickOption(option: String)
    }

}
