package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.activities.common.ChatActivity
import com.application.onovapplication.databinding.RvNotificationsBinding
import com.application.onovapplication.model.NotificationList
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.utils.Constants
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(
    val context: Context,
    private val notificationsList: ArrayList<NotificationList>
) : RecyclerView.Adapter<NotificationsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding =
            RvNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    var intent: Intent? = null
    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(notificationsList[position])


        holder.itemView.setOnClickListener {

            intent = Intent(context, ChatActivity::class.java)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent?.putExtra(
                Constants.USER_ID,
                notificationsList.get(position).sender_id.toString()
            )
            intent?.putExtra(Constants.USER_NAME, notificationsList.get(position).sennder_fullName.toString())
            intent?.putExtra(Constants.PHOTO,BaseUrl.photoUrl + notificationsList.get(position).sender_profilePic.toString())
            intent?.putExtra(Constants.FEED, "")
            intent?.putExtra(Constants.EVENT, "null")
            intent?.putExtra(Constants.FEEDTYPE, "")
            intent?.putExtra(Constants.USER_REF, notificationsList.get(position).notifyFrom.toString())
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            context.startActivity(intent)
        }

    }


    inner class RVHolder(val binding: RvNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationList: NotificationList) {

            binding.tvSubHeading.text = notificationList.notificationText
//            2022-04-18 02:55:56
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val date: Date = formatter.parse(notificationList.update_at!!) as Date
            val newFormat = SimpleDateFormat("dd-MMM hh:mm")
            val finalString = newFormat.format(date)

            binding.tvDate.text = finalString

        }

    }

}
