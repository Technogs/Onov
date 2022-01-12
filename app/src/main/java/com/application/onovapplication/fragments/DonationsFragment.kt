package com.application.onovapplication.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.AskDonationsActivity
import com.application.onovapplication.activities.common.AskToAddActivity
import com.application.onovapplication.adapters.DonationsAdapter
import com.application.onovapplication.databinding.FragmentCreatePostBinding
import com.application.onovapplication.databinding.FragmentDonationsBinding
import com.application.onovapplication.model.DonationData
import com.application.onovapplication.prefs.PreferenceManager
import com.application.onovapplication.viewModels.DonationViewModel
import com.google.android.material.snackbar.Snackbar



class DonationsFragment : Fragment(){

    private var ngoDonationsAdapter1: DonationsAdapter? = null
    private var ngoDonationsAdapter2: DonationsAdapter? = null
    val userPreferences: PreferenceManager by lazy {
        PreferenceManager(requireActivity())
    }

    lateinit var donation: DonationData
    lateinit var binding: FragmentDonationsBinding

    val donationViewModel by lazy { ViewModelProvider(this).get(DonationViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        binding = FragmentDonationsBinding.inflate(inflater, container, false)
        return binding.getRoot()     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donationViewModel.getDonations(requireActivity(),userPreferences.getUserREf())

        observeViewModel()
        binding.btnCreateDonation.setOnClickListener {
            val intent = Intent(requireContext(), AskToAddActivity::class.java)
            intent.putExtra("activity","donation")
            startActivity(intent)
        }

    }

    private fun observeViewModel() {

        donationViewModel.successful.observe(requireActivity(), Observer {

            if (it != null) {
                if (it) {
                    if (donationViewModel.status == "success") {
                        //   setLayout(eventViewModel.eventResponse)
                        donation=donationViewModel.dndata.data
                        if (donation.donorList.isNotEmpty()){
                          binding.donorLyt.visibility=View.VISIBLE
                            binding.noDnData.visibility=View.GONE
                            ngoDonationsAdapter1 = DonationsAdapter(requireActivity(),"donate",donation.donorList)
                            binding.rvSentDonations.adapter = ngoDonationsAdapter1}
                        if (donation.receivedList.isNotEmpty()){
                            binding.receiverLyt.visibility=View.VISIBLE
                            binding.noDnData.visibility=View.GONE
                            ngoDonationsAdapter2 = DonationsAdapter(requireActivity(),"receive",donation.receivedList)
                            binding.rvReceivedDonations.adapter = ngoDonationsAdapter2}

                    } else {
                        setError(donationViewModel.message)

                    }
                }
            } else {
                setError(donationViewModel.message)
            }

        })
    }

    fun setError(string: String) {

        val snackBar: Snackbar =
            Snackbar.make(requireActivity().findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.parseColor("#C2272D"))
        snackBar.show()
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            string,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}