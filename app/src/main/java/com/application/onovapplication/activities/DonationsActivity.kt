package com.application.onovapplication.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.AskDonationsActivity
import com.application.onovapplication.activities.common.AskToAddActivity
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.DonationsAdapter
import com.application.onovapplication.databinding.ActivityCommentBinding
import com.application.onovapplication.databinding.ActivityDonationsBinding
import com.application.onovapplication.databinding.ActivityDonationsNgoBinding
import com.application.onovapplication.model.DonationData
import com.application.onovapplication.viewModels.DonationViewModel
import com.application.onovapplication.viewModels.EventDetailViewModel

class DonationsActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityDonationsNgoBinding

    private var ngoDonationsAdapter1: DonationsAdapter? = null
    private var ngoDonationsAdapter2: DonationsAdapter? = null
    lateinit var donation: DonationData

    val donationViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_donations_ngo)
        binding = ActivityDonationsNgoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        donationViewModel.getDonations(this,userPreferences.getUserREf())
observeViewModel()

        binding.btnAskDonation.setOnClickListener {
          val intent=Intent(this@DonationsActivity,AskToAddActivity::class.java)
            intent.putExtra("activity","donation")
            startActivity(intent)

        }

    }

    private fun observeViewModel() {

        donationViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (donationViewModel.status == "success"){
                        donation=donationViewModel.dndata.data
                        if (donation.donorList.isNotEmpty()) binding.dnSentLyt.visibility=View.VISIBLE
                      if (donation.receivedList.isNotEmpty()){
                          binding.dnReceiveLyt.visibility=View.VISIBLE
                      }
                 if (donation.receivedList.isNullOrEmpty()&&donation.donorList.isNullOrEmpty() ){
                     binding.noData.visibility=View.VISIBLE

                        }
                        ngoDonationsAdapter1 = DonationsAdapter(this,"donate",donation.donorList)
                        ngoDonationsAdapter2 = DonationsAdapter(this,"receive",donation.receivedList)

                        binding.rvSentDonationsNgo.adapter = ngoDonationsAdapter1
                        binding.rvReceivedDonationsNgo.adapter = ngoDonationsAdapter2

                    } else {
                        setError(donationViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(donationViewModel.message)
            }

        })
    }
}