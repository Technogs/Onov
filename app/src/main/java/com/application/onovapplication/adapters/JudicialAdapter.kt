package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.databinding.ItemViewJudicialBinding
import com.application.onovapplication.model.JudicialData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide

class JudicialAdapter(val context: Context,var judicialList: List<JudicialData>) : RecyclerView.Adapter<JudicialAdapter.JudicialViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JudicialViewHolder {
        val binding = ItemViewJudicialBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return JudicialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JudicialViewHolder, position: Int) {
holder.bind(judicialList[position])
        Glide.with(context).load( BaseUrl.photoUrl+judicialList[position].memberPic).into(
            holder.binding.judicialPic)
    }
    override fun getItemCount(): Int {
        return judicialList.size
    }

    class JudicialViewHolder(val binding: ItemViewJudicialBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(judicialData: JudicialData) {


            binding.judiciaryAge.text = judicialData.age
            binding.judiciaryJoining.text = judicialData.appointmentDate
            binding.judiciaryName.text = judicialData.name
            binding.judiciaryScv.text = judicialData.scv
            binding.judiciaryAppointedBy.text = judicialData.appointmentBy


        }
    }
}