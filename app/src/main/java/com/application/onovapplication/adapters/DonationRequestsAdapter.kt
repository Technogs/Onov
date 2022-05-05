package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.activities.DonationDetailActivity
import com.application.onovapplication.databinding.RvPetitionsBinding
import com.application.onovapplication.model.AllRequestData

class DonationRequestsAdapter (val context: Context, val allRequestData: List<AllRequestData>
) : RecyclerView.Adapter<DonationRequestsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvPetitionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return allRequestData.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(allRequestData[position])
        holder.binding.mainlyt.setOnClickListener {
            val intent =  Intent(context, DonationDetailActivity::class.java)
            intent.putExtra("dnid",allRequestData[position].id)
            context.startActivity(intent)
        }

    }


    inner class RVHolder(val binding: RvPetitionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataItem: AllRequestData){
            binding.viewDetail.visibility=View.GONE
            binding.tv3.text="Donation Goal"
            binding.tv.text="Title"
            binding.tv2.text="Description"
            binding.senderName.text=dataItem.title
            binding.petitionTitle.text=dataItem.description
            binding.signatureCount.text=dataItem.donationGoal


        }

    }


}