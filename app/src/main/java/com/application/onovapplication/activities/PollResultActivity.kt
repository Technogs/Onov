package com.application.onovapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.PoleResultAdapter
import com.application.onovapplication.adapters.PollingSubmitAdapter
import com.application.onovapplication.databinding.ActivityAddPollingResultBinding
import com.application.onovapplication.databinding.ActivityPollResultBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.viewModels.PollingViewModel

class PollResultActivity : BaseAppCompatActivity() {
    var pollingResultAdapter: PoleResultAdapter? = null
    private lateinit var binding: ActivityPollResultBinding
    val pollingViewModel by lazy { ViewModelProvider(this).get(PollingViewModel::class.java) }
    lateinit var feed: FeedsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_poll_result)
        binding = ActivityPollResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        feed=intent.getParcelableExtra<FeedsData>("feed") as FeedsData

        pollingViewModel.getpollresult(this,feed.recordId.toString())
       observeViewModel()
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
//                        setLayout(eventViewModel.eventResponse)
                       // setError(pollingViewModel.message)
                        //startActivity(Intent(this, HomeTabActivity::class.java))

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