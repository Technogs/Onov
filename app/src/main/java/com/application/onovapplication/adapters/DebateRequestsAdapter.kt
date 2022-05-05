package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.activities.common.DebateDetailsActivity
import com.application.onovapplication.databinding.RvDebateRequestsBinding
import com.application.onovapplication.extensions.convertDateFormat
import com.application.onovapplication.model.DebateData

class DebateRequestsAdapter(val context: Context,val debateRequests: List<DebateData>
) : RecyclerView.Adapter<DebateRequestsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvDebateRequestsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return debateRequests.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RVHolder, position: Int) {
holder.bind(debateRequests[position])
        holder.binding.btnView.setOnClickListener {
            val intent = Intent(context, DebateDetailsActivity::class.java)
            intent.putExtra("debates",debateRequests[position])
            context.startActivity(intent)
        }
    }


    inner class RVHolder(val binding: RvDebateRequestsBinding) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dataItem:DebateData?){
            binding.titleValue.text=dataItem?.title

            binding.dateValue.text= convertDateFormat(
                dataItem?.date  ,
                "yyyy-MM-dd",
                "MMM dd,yyyy"
            ).toString()

            binding.timeValue.text=convertDateFormat(
                dataItem?.time,
                "HH:mm","hh:mm a"
            )
        }
    }



}
