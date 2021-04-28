package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.model.PredefinedTextsModel
import kotlinx.android.synthetic.main.rv_predefined_texts.view.*

class PredefinedTextsAdapter(
    val context: Context,
    val model: ArrayList<PredefinedTextsModel>,
    val predefinedTextClickInterface: PredefinedTextClickInterface
) : RecyclerView.Adapter<PredefinedTextsAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_predefined_texts, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(model[position])
    }


    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(predefinedTextsModel: PredefinedTextsModel) {
            itemView.text.text = predefinedTextsModel.text


            itemView.setOnClickListener {

                predefinedTextClickInterface.onClick(predefinedTextsModel)
            }

        }

    }


    interface PredefinedTextClickInterface {
        fun onClick(predefinedTextsModel: PredefinedTextsModel)


    }

}
