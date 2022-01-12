package com.application.onovapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.PollingAdapter
import com.application.onovapplication.adapters.PollingSubmitAdapter
import com.application.onovapplication.databinding.ActivityAddPollingResultBinding
import com.application.onovapplication.databinding.ActivityPetitionDetailsBinding
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.viewModels.PollingViewModel

class AddPollingResultActivity : BaseAppCompatActivity(),PollingSubmitAdapter.onClickOption {
    var pollingSubmitAdapter: PollingSubmitAdapter? = null
    lateinit var feed: FeedsData
     var soption: String=""
var options:ArrayList<String>?=null
    private lateinit var binding: ActivityAddPollingResultBinding
    val pollingViewModel by lazy { ViewModelProvider(this).get(PollingViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_polling_result)
        binding = ActivityAddPollingResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        feed=intent.getParcelableExtra<FeedsData>("feed") as FeedsData


initView()
        binding.submitPoll.setOnClickListener {
            pollingViewModel.polling(this,feed.recordId.toString(),
                userPreferences.getuserDetails()?.userRef.toString(),soption)
        }
        observeViewModel()

    }

    fun initView(){
        binding.etPollingTitle.setText(feed.polTitle)
        options = feed.polOptions?.split(",") as ArrayList<String>?
        pollingSubmitAdapter = PollingSubmitAdapter(this,options!!,this)
        binding.rvAddpolling.adapter=pollingSubmitAdapter
    }
    private fun observeViewModel() {

        pollingViewModel.successfulSubmitPoll.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (pollingViewModel.status == "success") {
//                        setLayout(eventViewModel.eventResponse)
    setError(pollingViewModel.message)
                        startActivity(Intent(this,HomeTabActivity::class.java))

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
        soption=option
    }
}