package com.application.onovapplication.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.databinding.DebatesRowBinding
import com.application.onovapplication.databinding.RvAllWinnersBinding
import com.application.onovapplication.debate.vlive.ui.main.MainActivity
import com.application.onovapplication.extensions.convertDateFormat
import com.application.onovapplication.model.AllWinnersList
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.model.LiveDebate
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class DebatesAdapter (val context: Context,val type: String,val liveDebate: List<LiveDebate>,val onclickDebate: onClickDebate) : RecyclerView.Adapter<DebatesAdapter.RVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = DebatesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RVHolder(binding)
    }

    override fun getItemCount(): Int {
        return liveDebate.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        holder.binding.debateLyt.setOnClickListener {
            if (type=="live") {
    onclickDebate.onDebateItemClick(liveDebate[position])

            }
        }


        holder.bind(liveDebate[position])
    }


    inner class RVHolder(val binding: DebatesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(liveDebate: LiveDebate) {

            binding.debateTopic.text = liveDebate.topic
            binding.debateTitle.text = liveDebate.title
            binding.debateVotes.text = liveDebate.vote
            binding.debateViews.text = liveDebate.view
            binding.debateDate.text = convertDateFormat( liveDebate.date  ,
                "yyyy-MM-dd",
                "MMM dd,yyyy")
            binding.debateDesc.text = liveDebate.message

          binding.debateCoverImage.setOnClickListener {

              val bitmapDrawable = context.getDrawable(R.drawable.onov_logo) as BitmapDrawable//imageView.drawable as BitmapDrawable
              val bitmap = bitmapDrawable.bitmap
              shareImageandText(bitmap, liveDebate)

            }
        }

        private fun shareImageandText(bitmap: Bitmap, liveDebate: LiveDebate) {
            val uri = getImageToShare(bitmap)
            val intent = Intent(Intent.ACTION_SEND)

            // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            // adding text to share
            intent.putExtra(Intent.EXTRA_TEXT, "Debate Details "+"\n"+liveDebate.title+liveDebate.message+"\n"+binding.debateDate.text.toString()+"\n"+ convertDateFormat(
                        liveDebate.time,
                        "HH:mm","hh:mm a"
                    )+"\n"+"Debate Duration: ${liveDebate.debateDuration}")

            // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

            // setting type to image
            intent.type = "*/*"

            // calling startactivity() to share
            context.startActivity(Intent.createChooser(intent, "Share Via"))
        }

        // Retrieving the url to share
        private fun getImageToShare(bitmap: Bitmap): Uri? {
            val imagefolder = File(context.getCacheDir(), "images")
            var uri: Uri? = null
            try {
                imagefolder.mkdirs()
                val file = File(imagefolder, "shared_image.png")
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                outputStream.flush()
                outputStream.close()
                uri = FileProvider.getUriForFile(context, "com.application.onovapplication.provider", file)
            } catch (e: java.lang.Exception) {
            }
            return uri
        }

    }
    interface onClickDebate{
       fun onDebateItemClick(liveDebate: LiveDebate)
    }

}