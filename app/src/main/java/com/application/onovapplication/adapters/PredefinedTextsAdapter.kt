package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.databinding.RvPredefinedTextsBinding
import com.application.onovapplication.model.PredefinedTextsModel

class PredefinedTextsAdapter(
    val context: Context,
    val model: ArrayList<PredefinedTextsModel>,
    val predefinedTextClickInterface: PredefinedTextClickInterface
) : RecyclerView.Adapter<PredefinedTextsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvPredefinedTextsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(model[position])
    }


    inner class RVHolder(val binding: RvPredefinedTextsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(predefinedTextsModel: PredefinedTextsModel) {
            binding.text.text = predefinedTextsModel.text


            itemView.setOnClickListener {

                predefinedTextClickInterface.onClick(predefinedTextsModel)
            }

        }

    }


    interface PredefinedTextClickInterface {
        fun onClick(predefinedTextsModel: PredefinedTextsModel)


    }

}
