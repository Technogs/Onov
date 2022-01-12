package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.ViewFollowersAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityDonorsBinding
import com.application.onovapplication.model.Follow
import com.application.onovapplication.viewModels.DonationViewModel


class DonorsActivity : BaseAppCompatActivity(),ViewFollowersAdapter.OnPeopleClick,View.OnClickListener {
    var viewFollowersAdapter: ViewFollowersAdapter? = null
    private lateinit var binding: ActivityDonorsBinding
//    var userrefs:ArrayList<String>
var userrefs = arrayListOf<String>()

    val donationViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }

    private lateinit var incbinding: ActionBarLayout2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_donors)
        binding = ActivityDonorsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val includedView: ActionBarLayout2Binding = binding.ab
        includedView.tvScreenTitle.text="Donor List"
        donationViewModel.getmydonner(this,userPreferences.getuserDetails()?.userRef.toString())
observeViewModel()

    }



    private fun observeViewModel() {

        donationViewModel.successfulMyDonors.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (donationViewModel.status == "success"){

                                viewFollowersAdapter =
            ViewFollowersAdapter(this, "people","donation",this,donationViewModel.dnrdata.donnerList)
        binding.rvFollowers.adapter = viewFollowersAdapter
//                        donation=donationViewModel.dndata.data
//                        if (donation.donorList.isNotEmpty()) binding.dnSentLyt.visibility= View.VISIBLE
//                        if (donation.receivedList.isNotEmpty()){
//                            binding.dnReceiveLyt.visibility= View.VISIBLE
//                        }
//                        if (donation.receivedList.isNullOrEmpty()&&donation.donorList.isNullOrEmpty() ){
//                            binding.noData.visibility= View.VISIBLE
//
//                        }
//                        ngoDonationsAdapter1 = DonationsAdapter(this,"donate",donation.donorList)
//                        ngoDonationsAdapter2 = DonationsAdapter(this,"receive",donation.receivedList)
//
//                        binding.rvSentDonationsNgo.adapter = ngoDonationsAdapter1
//                        binding.rvReceivedDonationsNgo.adapter = ngoDonationsAdapter2

                    } else {
                        setError(donationViewModel.message)
                       // finish()
                    }
                }
            } else {
                setError(donationViewModel.message)
            }

        })
    }

    override fun onPClick(datatem: Follow) {

    }

    override fun onCheckboxClick(datatem: Follow) {
       userrefs.add(datatem.userRef)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.submit->{
                val returnIntent = Intent()
                returnIntent.putExtra("result", userrefs)
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        }
    }

}