package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.DebateDetailsActivity
import com.application.onovapplication.databinding.RvDebateRequestsBinding
import com.application.onovapplication.model.DebateData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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
//            val firstApiFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
//            val date = LocalDate.parse(dataItem?.date , firstApiFormat)
            binding.dateValue.text= convertDateFormat(
                dataItem?.date  ,
                "yyyy-MM-dd",
                "MMM dd,yyyy"
            ).toString()

            binding.timeValue.text=dataItem?.time
        }
    }

    fun convertDateFormat(time: String?, inputType: String, outputType: String): String {
        val inputPattern = inputType
        val outputPattern = outputType
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str.toString()
    }


}
