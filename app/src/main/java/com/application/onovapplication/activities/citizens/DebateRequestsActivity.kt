package com.application.onovapplication.activities.citizens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.politicians.RequestDebateActivity
import com.application.onovapplication.adapters.DebateRequestsAdapter
import com.application.onovapplication.databinding.ActivityDebateRequestsBinding
import com.application.onovapplication.viewModels.DebatesViewModel

class DebateRequestsActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val debatesViewModel by lazy { ViewModelProvider(this).get(DebatesViewModel::class.java)}

    private var debateRequestsAdapter: DebateRequestsAdapter? = null
    private lateinit var binding: ActivityDebateRequestsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebateRequestsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        debatesViewModel.getDebateRequest(this,userPreferences.getuserDetails()?.userRef.toString())
observeViewModel()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCreateDebate -> {

                val intent = Intent(this, RequestDebateActivity::class.java)
                startActivity(intent)
            }

        }
    }


    private fun observeViewModel() {

        debatesViewModel.successful.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (debatesViewModel.status == "success") {
if (debatesViewModel.debates.data !=null){

    binding.noDebates.visibility=View.GONE
                        debateRequestsAdapter = DebateRequestsAdapter(this,debatesViewModel.debates.data)
    binding.rvDebateRequests.adapter = debateRequestsAdapter}

                    } else {
                        binding.noDebates.visibility=View.VISIBLE
                        setError(debatesViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(debatesViewModel.message)
            }

        })

//        debatesViewModel.successfulPeople.observe(this, androidx.lifecycle.Observer {
//            dismissDialog()
//            if (it != null) {
//                if (it) {
//                    if (profileViewModel.status == "success") {
//                        peoplesdata = profileViewModel.peoples!!.peopleData
//                        tv_followers.setText(peoplesdata!!.followCount.toString()+" Citizens")
//                        tv_following.setText(peoplesdata!!.followingCount.toString()+" Support")
//                        tv_donors.setText(peoplesdata!!.donnerCount.toString()+" Donors")
//                        setLayout(profileViewModel.userInfo!!)
//                        debatesAdapter = ViewDebatesAdapter(this, profileViewModel.userfeed!!.feedList,this)
//
//                        rv_profile_feeds.adapter = debatesAdapter
//
//                    } else {
//                        setError(profileViewModel.message)
//                        finish()
//                    }
//                }
//            } else {
//                setError(profileViewModel.message)
//            }
//
//        })

    }

    override fun onResume() {
        super.onResume()
        debatesViewModel.getDebateRequest(this,userPreferences.getuserDetails()?.userRef.toString())

    }
}