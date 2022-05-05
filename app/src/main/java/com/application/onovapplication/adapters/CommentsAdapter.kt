package com.application.onovapplication.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.CommentRowBinding
import com.application.onovapplication.model.CommentData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class CommentsAdapter(val context: Context, val cmntlist:List<CommentData>, val onReplyClick: OnReplyClick)
    : RecyclerView.Adapter<CommentsAdapter.RVHolder>() {


    var selectedItem=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = CommentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return cmntlist.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(cmntlist[position])
holder.binding.viewReply.setOnClickListener {
            onReplyClick.OnViewReplyClick(holder.binding.replyRecycler,cmntlist[position])
}
        holder.binding.likeComments.setOnClickListener {
            onReplyClick.OnLikeClick(holder.binding.likeComments,cmntlist[position])

}
        holder.binding.dislikeComments.setOnClickListener {
            onReplyClick.OnDisLikeClick(holder.binding.dislikeComments,cmntlist[position])

}
        holder.binding.postCmnt.setOnClickListener {
    if (holder.binding.etReplyBox.text.toString()=="")
        Toast.makeText(context, "write something before post", Toast.LENGTH_SHORT).show()
            else onReplyClick.OnClickReplyPost(holder.binding.replyLyt,holder.binding.etReplyBox,cmntlist[position])
}
    }


    inner class RVHolder(val binding: CommentRowBinding) :
//    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataitem: CommentData){
            if (dataitem.Liked=="1") {
                dataitem.like=true
                binding.likeComments.setColorFilter(context.resources.getColor(R.color.red))
            }else     dataitem.like=false
                if (dataitem.Disliked=="1") {
                dataitem.dislike=true
                    binding.dislikeComments.setColorFilter(context.resources.getColor(R.color.red))
            }else     dataitem.dislike=false
            if (dataitem.like==true)            binding.likeComments.setColorFilter(context.resources.getColor(R.color.red))
else   binding.likeComments.colorFilter=null
            if (dataitem.dislike==true)            binding.dislikeComments.setColorFilter(context.resources.getColor(R.color.red))
else   binding.dislikeComments.colorFilter=null
            binding.tvComment.text=dataitem.comment
            binding.replyLikeCount.text=dataitem.likeCount
            binding.replyDisLikeCount.text=dataitem.dislikeCount
            binding.replyCmntCount.text=dataitem.replyCount
            binding.tvNameCmnt.text=dataitem.fullName
            Glide.with(context).load(BaseUrl.photoUrl + dataitem.profilePic).apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(binding.ivCommentProfile)

            binding.replyCmnt.setOnClickListener {
               selectedItem=adapterPosition
               Log.e("CommentsAdapter"," reply_cmnt ")
               notifyDataSetChanged()
            }


            if(selectedItem==adapterPosition)
            {

                Log.e("CommentsAdapter"," adapterPosition ")
                binding.replyLyt.visibility=View.VISIBLE
            }

            else
            {
                Log.e("CommentsAdapter"," Not adapterPosition ")
                binding.replyLyt.visibility=View.GONE
            }


//            itemView.receivedFrom.text=dataitem.donateFrom

        }
    }

    interface OnReplyClick{
        fun OnClickReplyPost(replyLyt:RelativeLayout,replyMsg:EditText,commentData:CommentData)
        fun OnViewReplyClick(rview:RecyclerView,commentData:CommentData)
        fun OnLikeClick(likeText:ImageView,commentData:CommentData)
        fun OnDisLikeClick(likeText:ImageView,commentData:CommentData)
    }
}