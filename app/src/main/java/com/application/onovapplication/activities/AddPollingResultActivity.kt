package com.application.onovapplication.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.PollingSubmitAdapter
import com.application.onovapplication.databinding.ActivityAddPollingResultBinding
import com.application.onovapplication.model.ActivePoll
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.PollingViewModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

class AddPollingResultActivity : BaseAppCompatActivity(), PollingSubmitAdapter.onClickOption,
    View.OnClickListener {
    var pollingSubmitAdapter: PollingSubmitAdapter? = null
    var feed: FeedsData? = null
    var activePoll: ActivePoll? = null
    var ismultiple: String = ""
    var userRef: String = ""
    var title: String = ""
    var totalvote: String = ""
    var id: String = ""
    var dateTime: String = ""
    var options: ArrayList<String>? = null
    var pOptions: ArrayList<String>? = ArrayList()
    private lateinit var binding: ActivityAddPollingResultBinding
    val pollingViewModel by lazy { ViewModelProvider(this).get(PollingViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPollingResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (intent.getParcelableExtra<FeedsData>("feed") != null)
            feed = intent.getParcelableExtra<FeedsData>("feed") as FeedsData
        if (intent.getParcelableExtra<ActivePoll>("poll") != null)
            activePoll = intent.getParcelableExtra<ActivePoll>("poll") as ActivePoll
        if (feed != null) {
            ismultiple = feed?.polisMultiple.toString()
            userRef = feed?.userRef.toString()
            title = feed?.title.toString()
            id = feed?.recordId.toString()
            totalvote = feed?.totalVote.toString()
            dateTime = feed?.polTillDateTime.toString()
//           val option1 = feed?.polOptions?.split(",") as ArrayList<String>?
            options = feed?.polOptions as ArrayList<String>?
//            if (feed?.polOptions!!.contains(","))options=feed?.polOptions?.split(",") as ArrayList<String>?
//            else {
//                options?.add(feed?.polOptions.toString())
//                Toast.makeText(this, "" + options, Toast.LENGTH_SHORT).show()
//            }
            if (feed?.pollImage.isNullOrEmpty()) {

                binding.ivFeed.visibility = View.GONE
            } else {
                binding.ivFeed.visibility = View.VISIBLE
                Glide.with(this).load(BaseUrl.photoUrl + feed?.pollImage)
                    .into(binding.ivFeed)
            }

            binding.ivFeed.setOnClickListener { showDialog(feed?.pollImage.toString()) }
        } else if (activePoll != null) {
            ismultiple = activePoll?.isMultiple.toString()
            userRef = activePoll?.createBy.toString()
            title = activePoll?.title.toString()
            totalvote = activePoll?.totalVote.toString()
            id = activePoll?.id.toString()
            dateTime = activePoll?.tillDateTime.toString()
            options = activePoll?.options as ArrayList<String>?
            if (activePoll?.pollImage.isNullOrEmpty()) {

                binding.ivFeed.visibility = View.GONE
            } else {
                binding.ivFeed.visibility = View.VISIBLE
                Glide.with(this).load(BaseUrl.photoUrl + activePoll?.pollImage)
                    .into(binding.ivFeed)
            }

            binding.ivFeed.setOnClickListener { showDialog(activePoll?.pollImage.toString()) }
        }
        initView()
        observeViewModel()

    }

    fun initView() {
        if (userRef == userPreferences.getuserDetails()?.userRef.toString())
            binding.pollEnd.visibility = View.VISIBLE
        else binding.pollEnd.visibility = View.GONE
        binding.etPollingTitle.text = title
        binding.voteTotal.text = "Total Votes: " + totalvote
        binding.pollEndTime.text = "Poll closes: " + convertDateFormat(
            dateTime,
            "yyyy-MM-dd hh:mm:ss",
            "MMM dd, hh:mm a"
        ).trim()
//        options = feed.polOptions?.split(",") as ArrayList<String>?
        if (options!=null) {
            pollingSubmitAdapter = PollingSubmitAdapter(this, options!!, this, ismultiple)
            binding.rvAddpolling.adapter = pollingSubmitAdapter
        }
    }

    private fun showDialog(pollImage: String) {

        var dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

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

        pollingViewModel.successfulSubmitPoll.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (pollingViewModel.status == "success") {
                        setError(pollingViewModel.message)
                        startActivity(Intent(this, HomeTabActivity::class.java))

                    } else {
                        setError(pollingViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(pollingViewModel.message)
            }

        })
        pollingViewModel.successfulEndPoll.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (pollingViewModel.status == "success") {
                        setError(pollingViewModel.message)
                        startActivity(Intent(this, HomeTabActivity::class.java))

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

    override fun onClickOptions(option: String) {
        pOptions?.add(option)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.changeVote -> {
                pollingSubmitAdapter = PollingSubmitAdapter(this, options!!, this, ismultiple)
                binding.rvAddpolling.adapter = pollingSubmitAdapter
            }
            R.id.pollEnd -> {
                pollingViewModel.endpoll(
                    this,
                    userPreferences.getuserDetails()?.userRef.toString(),
                    id
                )
            }
            R.id.submitPoll -> {
                if (pOptions.isNullOrEmpty()) setError("Please select option")
                else {
                    showDialog()
                    val opt: String = TextUtils.join(",", pOptions!!)

                    pollingViewModel.polling(
                        this, id,
                        userPreferences.getuserDetails()?.userRef.toString(), opt
                    )
                }
            }
        }
    }
}