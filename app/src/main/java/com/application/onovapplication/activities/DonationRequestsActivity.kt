package com.application.onovapplication.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.DonationRequestsAdapter
import com.application.onovapplication.databinding.ActivityDonationRequestsBinding
import com.application.onovapplication.model.AllRequestData
import com.application.onovapplication.viewModels.DonationViewModel

class DonationRequestsActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityDonationRequestsBinding
    var donationRequestsAdapter: DonationRequestsAdapter?  = null
    lateinit var allRequestData:List<AllRequestData>
    var pId:String=""
    private val donationViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationRequestsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        donationViewModel.getAllDonationRequest(this,userPreferences.getuserDetails()?.userRef.toString())
        showDialog()
        observeViewModel()
    }

    private fun observeViewModel() {

        donationViewModel.successfulRequests.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (donationViewModel.status == "success") {
                        allRequestData=donationViewModel.dnRequestsResponse.allRequestData
                        if (allRequestData.isNullOrEmpty()) binding.noEventsLayout.visibility=View.VISIBLE
                   else     {
                            donationRequestsAdapter = DonationRequestsAdapter(this, allRequestData)
                            binding.rvPetition.adapter = donationRequestsAdapter
                            donationRequestsAdapter!!.notifyDataSetChanged()
                        }

                    } else {
                        setError(donationViewModel.message)
                    }
                }
            } else {
                setError(donationViewModel.message)
            }

        })}}