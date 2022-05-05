package com.application.onovapplication.activities

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.PoleResultAdapter
import com.application.onovapplication.databinding.ActivityPollResultBinding
import com.application.onovapplication.model.ActivePoll
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.PollingViewModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

class PollResultActivity : BaseAppCompatActivity() {
    var pollingResultAdapter: PoleResultAdapter? = null
    private lateinit var binding: ActivityPollResultBinding
    val pollingViewModel by lazy { ViewModelProvider(this).get(PollingViewModel::class.java) }
    var feed: FeedsData?=null
     var activePoll: ActivePoll?=null
    var id: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (intent.getParcelableExtra<FeedsData>("feed")!=null)
        feed=intent.getParcelableExtra<FeedsData>("feed") as FeedsData
        if (intent.getParcelableExtra<ActivePoll>("poll")!=null)
        activePoll=intent.getParcelableExtra<ActivePoll>("poll") as ActivePoll
if (feed!=null) {
    id = feed?.recordId.toString()
    binding.voteTotal.setText("Total Votes:"+feed?.totalVote)
    if (feed?.pollImage.isNullOrEmpty()) {

        binding.ivFeed.visibility = View.GONE
    }else{
        binding.ivFeed.visibility = View.VISIBLE
        Glide.with(this).load(BaseUrl.photoUrl + feed?.pollImage)
            .into(binding.ivFeed)
    }
    binding.ivFeed.setOnClickListener { showDialog(feed?.pollImage.toString()) }
}else if (activePoll!=null) {
    id = activePoll?.id.toString()
    if (activePoll?.pollEndStatus=="0") binding.pollEndTime.setText("Poll Closes: "+convertDateFormat(
        activePoll?.tillDateTime,
        "yyyy-MM-dd hh:mm:ss",
        "MMM dd, hh:mm a"
    ).trim())
    binding.voteTotal.setText("Total Votes:"+activePoll?.totalVote)
    if (activePoll?.pollImage.isNullOrEmpty()) {

        binding.ivFeed.visibility = View.GONE
    }else{
        binding.ivFeed.visibility = View.VISIBLE
        Glide.with(this).load(BaseUrl.photoUrl + activePoll?.pollImage)
            .into(binding.ivFeed)
    }

    binding.ivFeed.setOnClickListener { showDialog(activePoll?.pollImage.toString()) }
}
        pollingViewModel.getpollresult(this,id,userPreferences.getuserDetails()?.userRef.toString())
       observeViewModel()
    }




    private fun showDialog(pollImage: String) {

        var dialog = Dialog(this)

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
        Glide.with(this).load(BaseUrl.photoUrl + pollImage)
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
    private fun observeViewModel() {

        pollingViewModel.successfulResultPoll.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (pollingViewModel.status == "success") {
                        binding.etPollingTitle.setText(pollingViewModel.pollResultResponse.pollData.title)
                        pollingResultAdapter = PoleResultAdapter(this,pollingViewModel.pollResultResponse.pollResult)
                        binding.rvAddpolling.adapter=pollingResultAdapter
                        pollingResultAdapter!!.notifyDataSetChanged()

                    } else {
                        setError(pollingViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(pollingViewModel.message)
            }

        })


    }
}