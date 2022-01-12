package com.application.onovapplication.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.RvStatsBinding

class MessageAdapter (val messages: ArrayList<MessageClass>, val itemClick: (MessageClass) -> Unit) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(messages[position])
    }

    override fun getItemCount() = messages.size

    class ViewHolder(view: View, val itemClick: (MessageClass) -> Unit) : RecyclerView.ViewHolder(view) {
lateinit var messageAdapterMessageItem:TextView
        fun bindForecast(message: MessageClass) {
            with(message) {
                messageAdapterMessageItem=        itemView.findViewById(R.id.messageAdapterMessageItem)
                messageAdapterMessageItem.text = message.text
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}