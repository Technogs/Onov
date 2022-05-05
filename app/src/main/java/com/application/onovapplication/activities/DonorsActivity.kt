package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityDonorsBinding
import com.application.onovapplication.model.DonorsResponse
import com.application.onovapplication.model.Follow
import com.application.onovapplication.viewModels.DonationViewModel


class DonorsActivity : BaseAppCompatActivity(), ViewFollowersAdapter.OnPeopleClick,
    View.OnClickListener {
    var viewFollowersAdapter: ViewFollowersAdapter? = null
    private lateinit var binding: ActivityDonorsBinding
    lateinit var donorsResponse: DonorsResponse
    var userrefs = arrayListOf<String>()
    var donors : MutableList<Follow>?=null

    val donationViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }

    private lateinit var incbinding: ActionBarLayout2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val includedView: ActionBarLayout2Binding = binding.ab
        includedView.tvScreenTitle.text = "Donor List"
        donationViewModel.getmydonner(this, userPreferences.getuserDetails()?.userRef.toString(),"")
        binding.selectAllCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                viewFollowersAdapter =
                    ViewFollowersAdapter(
                        this,
                        "people",
                        "donation", "","check",
                        this,
                        donorsResponse.donnerList
                    )
                binding.rvFollowers.adapter = viewFollowersAdapter
            }else{
                viewFollowersAdapter =
                    ViewFollowersAdapter(
                        this,
                        "people",
                        "donation", "","",
                        this,
                        donorsResponse.donnerList
                    )
                binding.rvFollowers.adapter = viewFollowersAdapter
            }
        }
        observeViewModel()

        binding.selectAllCheckBox.setOnClickListener {
            if (binding.selectAllCheckBox.isChecked == true) {

                userrefs.clear()
                for (i in 0..donorsResponse.donnerList.size - 1) {
                    userrefs.add(donorsResponse.donnerList[i].userRef2)
                }
                Toast.makeText(this, "donors"+userrefs.size, Toast.LENGTH_SHORT).show()
            }else if(binding.selectAllCheckBox.isChecked == false){
                userrefs.clear()
            }
        }

    }


    private fun observeViewModel() {

        donationViewModel.successfulMyDonors.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (donationViewModel.status == "success") {

                        donorsResponse = donationViewModel.dnrdata
                        if (donorsResponse.donnerList.isNotEmpty()) {
                            binding.rvFollowers.visibility = View.VISIBLE
                            binding.noDataLayout.visibility=View.GONE
                            viewFollowersAdapter =
                                ViewFollowersAdapter(
                                    this,
                                    "people",
                                    "donation", "","",
                                    this,
                                    donorsResponse.donnerList
                                )
                            binding.rvFollowers.adapter = viewFollowersAdapter
                        }else {
                            binding.noDataLayout.visibility = View.VISIBLE
                            binding.rvFollowers.visibility = View.GONE
                        }


                    } else { setError(donationViewModel.message) }
                }
            } else { setError(donationViewModel.message) }

        })
    }

    override fun onPClick(datatem: Follow) {

    }

    override fun onCheckboxClick(datatem: Follow) {
        userrefs.add(datatem.userRef2)
    }

    override fun onRemoveFollow(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onRemoveSupport(datatem: Follow) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submit -> {
                val returnIntent = Intent()
                returnIntent.putExtra("result", userrefs)
                setResult(RESULT_OK, returnIntent)
                finish()
            }  R.id.searchBtn -> {
               if (binding.serchText.text.toString()==""){
                   setError("enter a text")
               }else{
                   donationViewModel.getmydonner(this, userPreferences.getuserDetails()?.userRef.toString(),binding.serchText.text.toString())

               }
               }
            }
        }
    }

