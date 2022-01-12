
package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.EventDetailsActivity
import com.application.onovapplication.databinding.RvEventAttendeesBinding
import com.application.onovapplication.databinding.RvEventsBinding
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EventsAdapter  (val context: Context,val events:List<EventData>, val onEventClick: OnEventClick) : RecyclerView.Adapter<EventsAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)    }


    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
holder.bind(events[position])
        holder.binding.eventLyt.setOnClickListener {
            val intent = Intent(context , EventDetailsActivity::class.java)
           intent.putExtra("data",events[position])
            context.startActivity(intent)


        }

        holder.binding.eventCmntLyt.setOnClickListener { onEventClick.OnEventCommentClick(events[position]) }
        holder.binding.eventDislikeLyt.setOnClickListener {
            onEventClick.OnEventLikeClick("0",events[position]) }
        holder.binding.eventLikeLyt.setOnClickListener {
            onEventClick.OnEventLikeClick("1",events[position])  }
        holder.binding.eventShareLyt.setOnClickListener {onEventClick.OnEventShareClick(events[position])  }
    }


    inner class RVHolder(val binding: RvEventsBinding) : RecyclerView.ViewHolder(binding.root) {
fun bind(dataItem:EventData?){
    binding.eventTitle.text=dataItem?.title
    binding.tvEventLikeCount.text=dataItem?.likeCount
    binding.tvEventDisLikeCount.text=dataItem?.dislikeCount
    binding.tvEventCommentCount.text=dataItem?.CommentCount
    binding.eventTitle.text=dataItem?.title
    binding.eventTitle.text=dataItem?.title
    Glide.with(context).load(BaseUrl.photoUrl +dataItem?.cover_image).apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24)).into(binding.eventPic)
    if (dataItem?.Liked == "1") {
        binding.likeEvt.setColorFilter(context.resources.getColor(R.color.red))
        dataItem.like = true
        dataItem.dislike = false
    } else if (dataItem?.Disliked == "1") {
        binding.dislikeevt.setColorFilter(context.resources.getColor(R.color.red))
        dataItem?.like = false
        dataItem?.dislike = true
    }
    if (dataItem?.like == true) {
        binding.likeEvt.setColorFilter(context.resources.getColor(R.color.red))
    } else {
        binding.likeEvt.colorFilter = null
    }
    if (dataItem?.dislike == true) binding.dislikeevt.setColorFilter(
        context.resources.getColor(R.color.red)
    )
    else binding.dislikeevt.colorFilter = null

}

    }


   interface OnEventClick{
       fun OnEventLikeClick(type:String,dataItem: EventData?)
       fun OnEventShareClick(dataItem: EventData?)
       fun OnEventCommentClick(dataItem: EventData?)
   }
}

