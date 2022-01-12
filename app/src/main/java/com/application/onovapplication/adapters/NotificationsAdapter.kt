package com.application.onovapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvMoreBinding
import com.application.onovapplication.databinding.RvNotificationsBinding
import com.application.onovapplication.model.NotificationList
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(
    val context: Context,
    private val notificationsList: ArrayList<NotificationList>
) : RecyclerView.Adapter<NotificationsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.bind(notificationsList[position])
    }


    inner class RVHolder(val binding: RvNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationList: NotificationList) {

            binding.tvSubHeading.text = notificationList.notificationText

            val formatter: DateFormat = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
            val date: Date = formatter.parse(notificationList.updateAt!!) as Date
            val newFormat = SimpleDateFormat("dd-MM-yyyy")
            val finalString = newFormat.format(date)

            binding.tvDate.text = finalString


        }

    }

}
