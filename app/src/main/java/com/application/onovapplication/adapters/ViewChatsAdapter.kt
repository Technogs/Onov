package com.application.onovapplication.adapters

//https://getstream.io/tutorials/android-chat/

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.RvChatsBinding
import com.application.onovapplication.model.ChatModel
import com.application.onovapplication.model.Follow
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ViewChatsAdapter(
    val context: Context,
    val chats: List<Follow>,
    var onMessageCallback: OnMessageClickListener,
    var onMessageImage: OnMessageClickLImage,
    var user: Int
) : RecyclerView.Adapter<ViewChatsAdapter.RVHolder>() {
    private var firebaseDataBase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        if (user == 1) holder.binding.cLastMessage.visibility = View.GONE
        holder.bind(chats[position])
        holder.binding.chatLyt.setOnClickListener {
            onMessageCallback.onMessageClickListener(chats[position])

        }
        holder.binding.ivChatProfile.setOnClickListener {
            onMessageImage.onMessageImageClick(chats[position])

        }

        if (chats.get(position).lastSender.equals("")){
            holder.binding.count.visibility=View.GONE
        }else if (chats.get(position).lastSender.equals(chats.get(position).fromRef)){
            holder.binding.count.visibility=View.GONE
        }else{
            if (!chats.get(position).msgCount.equals("0")){
                holder.binding.count.visibility=View.VISIBLE
                holder.binding.count.text=chats.get(position).msgCount
            }else{
                holder.binding.count.visibility=View.GONE

            }

        }

    }


    inner class RVHolder(val binding: RvChatsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: Follow) {
            binding.tvChatDate.text = getCurrentDate()
            binding.chatUserName.text = dataItem.fullName
            Glide.with(context).load(BaseUrl.photoUrl + dataItem.profilePic)
                .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
                .into(binding.ivChatProfile)

            val senderId =
                (context as BaseAppCompatActivity).userPreferences.getuserDetails()?.id?.trim()
                    ?.toInt()
            val receiverId = dataItem.user_id?.trim()?.toInt()
            var connectionid = "0"
            if (senderId != null) {
                if (senderId < receiverId!!)
                    connectionid = "$senderId-$receiverId"
                else
                    connectionid = "$receiverId-$senderId"
            }

            val chatReference: DatabaseReference =
                firebaseDataBase.reference.child("Chats").child(connectionid)

            chatReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val query = chatReference.orderByKey().limitToLast(1)
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                val chatTime = context.convertDateFormat(
                                    child.child("message_date").value.toString(),
                                    "yyyy-MM-dd hh:mm:ss",
                                    "MMM dd, hh:mm a"
                                ).trim()
                                binding.tvChatDate.text = chatTime

                                val lastMessage = child.child("message").value.toString()
                                val lastMessageType = child.child("message_type").value.toString()

                                var fileUrl = child.child("fileUrl").value.toString().trim()
                                if (lastMessage.trim().isNotEmpty()) {
                                    binding.cLastMessage.text = lastMessage
                                    return
                                }


                                if (lastMessageType.equals("image")) {
                                    binding.cLastMessage.text = "image"
                                    binding.cLastMessage.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.ic_baseline_photo_24,
                                        0,
                                        0,
                                        0
                                    )

                                    return
                                }
                                if (lastMessageType.equals("feed")) {
                                    binding.cLastMessage.text = "shared"
                                    binding.cLastMessage.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.ic_baseline_photo_24,
                                        0,
                                        0,
                                        0
                                    )

                                    return
                                }
                                if (lastMessageType.equals("event")) {
                                    binding.cLastMessage.text = "shared"
                                    binding.cLastMessage.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.ic_baseline_photo_24,
                                        0,
                                        0,
                                        0
                                    )
//                                    itemView.linearNoMessage.show()
//                                    itemView.tvMessageText.hide()
                                    return
                                }

//                                if (lastMessageType.equals("audio")) {
//
//
//                                    binding.linearNoMessage.show()
//                                    binding.tvMessageText.hide()
//                                    binding.tvPhoto.text = "audio"
//                                    binding.ivDefaultPhoto.loadImage(android.R.drawable.ic_btn_speak_now)
//
//
//                                    /*  binding.linearNoMessage.hide()
//                                      binding.tvMessageText.show()
//                                      binding.tvMessageText.text = "audio"*/
//
//                                    return
//                                } else {
//                                    binding.linearNoMessage.hide()
//                                    binding.tvMessageText.show()
//                                    binding.tvMessageText.text = "Tap to start chat"
//                                }


                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                        }
                    })

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        }


    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MMM dd, HH:mm a" + "")
        return sdf.format(Date())
    }

    interface OnMessageClickListener {
        fun onMessageClickListener(data: Follow)
    }

    interface OnMessageClickLImage {
        fun onMessageImageClick(data: Follow)
    }
}
