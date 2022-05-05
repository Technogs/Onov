package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.activities.ViewSignActivity
import com.application.onovapplication.databinding.RvSignBinding
import com.application.onovapplication.model.Sign
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide

class PetitionSignAdapter (val context: Context, val petitionSign: List<Sign>
) : RecyclerView.Adapter<PetitionSignAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvSignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)

    }

    override fun getItemCount(): Int {
        return petitionSign.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(petitionSign[position])
        holder.binding.root.setOnClickListener {
            val intent =  Intent(context, ViewSignActivity::class.java)
            intent.putExtra("pid",petitionSign[position].id)
            context.startActivity(intent)
        }

    }


    inner class RVHolder(val binding: RvSignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(petitionSigns: Sign){
            binding.signName.text=petitionSigns.fullName
            Glide.with(context).load( BaseUrl.photoUrl+petitionSigns.signImageUrl).into(binding.signImage)

        }

    }

}