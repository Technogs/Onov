package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.databinding.RvDonationsBinding
import com.application.onovapplication.extensions.convertDateFormat
import com.application.onovapplication.model.ReceivedList

class DonationsAdapter (val context: Context,val type: String,val donorlist:List<ReceivedList>)
    : RecyclerView.Adapter<DonationsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvDonationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return donorlist.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(donorlist[position])

    }


    inner class RVHolder(val binding: RvDonationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
fun bind(dataitem:ReceivedList){
    if (type=="donate"){
        binding.receivedFrom.text=dataitem.donateTo
    }else{
        binding.receivedFrom.text=dataitem.donateFrom

    }
    binding.donationAmount.text=dataitem.amount
    binding.donationDate.text=convertDateFormat(
        dataitem.created_at,
        "yyyy-MM-dd hh:mm:ss",
        "MMM dd, hh:mm a"
    ).trim()


}
    }

}