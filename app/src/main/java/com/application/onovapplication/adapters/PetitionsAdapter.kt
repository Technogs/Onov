package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.activities.PetitionDetailsActivity
import com.application.onovapplication.databinding.RvPetitionsBinding
import com.application.onovapplication.model.PetitionArray

class PetitionsAdapter(val context: Context, val petitionArray: List<PetitionArray>, val past: String
) : RecyclerView.Adapter<PetitionsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
            val binding = RvPetitionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)

    }

    override fun getItemCount(): Int {
        return petitionArray.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
holder.bind(petitionArray[position])
        holder.binding.mainlyt.setOnClickListener {
            val intent =  Intent(context, PetitionDetailsActivity::class.java)
            intent.putExtra("petitions",petitionArray[position])
            intent.putExtra("past",past)
            context.startActivity(intent)
        }

    }


    inner class RVHolder(val binding: RvPetitionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(petitionArray: PetitionArray){
                binding.petitionTitle.text=petitionArray.title
                binding.senderName.text=petitionArray.sentFrom
                binding.signatureCount.text=petitionArray.signCount
            }

    }

}