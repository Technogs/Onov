package com.application.onovapplication.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.model.FeedData
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.donate_dialog.*
import kotlinx.android.synthetic.main.rv_debates.view.*


class ViewDebatesAdapter(
    val context: Context,
    val feedData: ArrayList<FeedData>
) : RecyclerView.Adapter<ViewDebatesAdapter.RVHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_debates, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return feedData.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {


        holder.bind(feedData[position])
        // holder.itemView.ll.weightSum = 4f


        holder.itemView.donateLayout.setOnClickListener {
            openDonationsDialog()
        }
    }

    inner class RVHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(feedData: FeedData) {

            Glide.with(context).load(BaseUrl.photoUrl + feedData.profilePic)
                .into(itemView.ivChatProfile)
            itemView.userName.text = feedData.name
            itemView.feedTitle.text = feedData.title
            itemView.feedDescription.text = feedData.description


            if (feedData.filePath.isNullOrEmpty()) {
                itemView.feedMedia.visibility = View.GONE
            } else {
                itemView.feedMedia.visibility = View.VISIBLE

            }

            if (feedData.description.isNullOrEmpty()) {
                itemView.feedDescription.visibility = View.GONE
            } else {
                itemView.feedDescription.visibility = View.VISIBLE
            }
            if (feedData.title.isNullOrEmpty()) {
                itemView.feedTitle.visibility = View.GONE
            } else {
                itemView.feedTitle.visibility = View.VISIBLE
            }

            when (feedData.recordType) {

                "donationRequest" -> {
                    itemView.donateLayout.visibility = View.VISIBLE
                }

                else -> {
                    itemView.donateLayout.visibility = View.GONE
                }
            }


            when (feedData.fileType) {
                "document" -> {
                    itemView.wbFeed.visibility = View.VISIBLE
                    itemView.vvFeed.visibility = View.GONE
                    itemView.ivFeed.visibility = View.GONE

                    itemView.wbFeed.settings.javaScriptEnabled = true

                    itemView.wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
                    itemView.wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

                    itemView.wbFeed.loadUrl(
                        "https://docs.google.com/gview?embedded=true&url=".plus(
                            BaseUrl.photoUrl + feedData.filePath
                        )
                    )
                    //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))

                }

                "video" -> {
                    itemView.wbFeed.visibility = View.GONE
                    itemView.vvFeed.visibility = View.VISIBLE
                    itemView.ivFeed.visibility = View.GONE

                    val mc = MediaController(context)
                    itemView.vvFeed.setMediaController(mc)
                    itemView.vvFeed.setVideoPath(BaseUrl.photoUrl + feedData.filePath)
                    // itemView.vvFeed.start()
                }

                "image" -> {
                    itemView.wbFeed.visibility = View.GONE
                    itemView.vvFeed.visibility = View.GONE
                    itemView.ivFeed.visibility = View.VISIBLE

                    Glide.with(context).load(BaseUrl.photoUrl + feedData.filePath)
                        .into(itemView.ivFeed)

                }
            }


        }

    }

    private fun openDonationsDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.donate_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show()

        dialog.submitDonation.setOnClickListener {
            dialog.dismiss()
        }

        dialog.cancelDonation.setOnClickListener {
            dialog.dismiss()

        }

    }

}
