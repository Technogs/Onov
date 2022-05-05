package com.application.onovapplication.adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.*
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.AddPollingResultActivity
import com.application.onovapplication.activities.PollResultActivity
import com.application.onovapplication.activities.common.ProfileActivity2
import com.application.onovapplication.databinding.RvPollHistoryBinding
import com.application.onovapplication.extensions.convertDateFormat
import com.application.onovapplication.model.ActivePoll
import com.application.onovapplication.repository.BaseUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

class PollHistoryAdapter (val context: Context, val pollResult: List<ActivePoll>, val active: String) : RecyclerView.Adapter<PollHistoryAdapter.RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val binding = RvPollHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHolder(binding)
    }

    override fun getItemCount(): Int { return pollResult.size }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
          holder.bind(pollResult[position])
        holder.binding.ivChatProfile.setOnClickListener {
            val intent = Intent(context, ProfileActivity2::class.java)
            intent.putExtra("type", "other")
            intent.putExtra("usrRef",pollResult[position].createBy )
            context.startActivity(intent)
        }
        holder.binding.userName.setOnClickListener {
            val intent = Intent(context, ProfileActivity2::class.java)
            intent.putExtra("type", "other")
            intent.putExtra("usrRef",pollResult[position].createBy )
            context.startActivity(intent)
        }
    }
    inner class RVHolder(val binding: RvPollHistoryBinding) :
        RecyclerView.ViewHolder(binding.root){
fun bind(dataItem:ActivePoll){
  binding.pollQue.setText( dataItem.title)
  binding.totalVote.setText("Total Votes: "+ dataItem.totalVote)
  binding.userName.setText( dataItem.fullName)

    if (dataItem.pollImage.isNullOrEmpty()) {

        binding.ivFeed.visibility = View.GONE
    }else{
        binding.ivFeed.visibility = View.VISIBLE
        Glide.with(context).load(BaseUrl.photoUrl + dataItem.pollImage)
            .into(binding.ivFeed)
    }

    if (dataItem.isPublic=="1")binding.publicPoll.visibility=View.VISIBLE
    Glide.with(context).load(BaseUrl.photoUrl + dataItem.profilePic)
        .apply(RequestOptions().placeholder(R.drawable.ic_baseline_account_circle_24))
        .into(binding.ivChatProfile)
    if (active=="1") {

        binding.pollEnded.setText("Poll closes:" + convertDateFormat(
            dataItem.tillDateTime,
            "yyyy-MM-dd hh:mm:ss",
            "MMM dd, hh:mm a"
        ).trim())
        if (dataItem.isPoll=="0") {
            binding.viewResult.setText("Vote")
            binding.viewResult.setOnClickListener {
                val intent = Intent(context, AddPollingResultActivity::class.java)
                intent.putExtra("poll", dataItem)
                context.startActivity(intent)
            }
        } else if (dataItem.isPoll=="1") {
            binding.viewResult.setText("View Result")
            binding.viewResult.setOnClickListener {
                val intent = Intent(context, PollResultActivity::class.java)
                intent.putExtra("poll", dataItem)
                context.startActivity(intent)
            }
        }

    }else{
        binding.viewResult.setOnClickListener {
        val intent = Intent(context, PollResultActivity::class.java)
        intent.putExtra("poll", dataItem)
        context.startActivity(intent)
        }
    }

    binding.ivFeed.setOnClickListener { showDialog(dataItem) }

}

        private fun showDialog(mdData: ActivePoll) {

            var dialog = Dialog(context)

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_layout)
            dialog.getWindow()?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            val ivFeed = dialog.findViewById(R.id.ivFeed) as ImageView
            val cross = dialog.findViewById(R.id.close) as ImageView
            val vvFeed = dialog.findViewById(R.id.vvFeed) as VideoView

            val idExoPlayerVIew = dialog.findViewById(R.id.idExoPlayerVIew) as SimpleExoPlayerView
            val wbFeed = dialog.findViewById(R.id.wbFeed) as WebView
            cross.setOnClickListener { dialog.dismiss() }

                wbFeed.visibility = View.GONE
                vvFeed.visibility = View.GONE
                idExoPlayerVIew.visibility = View.GONE
                ivFeed.visibility = View.VISIBLE
                Glide.with(context).load(BaseUrl.photoUrl + mdData.pollImage)
                    .into(ivFeed)

            dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog?.cancel()
                    return@OnKeyListener true

                }
                false
            })
            dialog.show()

        }
    }

}
