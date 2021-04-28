package com.application.onovapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.AskDonationsActivity
import com.application.onovapplication.activities.common.CreateDonationActivity
import com.application.onovapplication.adapters.DonationsAdapter
import kotlinx.android.synthetic.main.fragment_donations.*


class DonationsFragment : Fragment() {

    private var ngoDonationsAdapter1: DonationsAdapter? = null
    private var ngoDonationsAdapter2: DonationsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ngoDonationsAdapter1 = DonationsAdapter(requireContext())
        ngoDonationsAdapter2 = DonationsAdapter(requireContext())


        rvSentDonations.adapter = ngoDonationsAdapter1
        rvReceivedDonations.adapter = ngoDonationsAdapter2

        btnCreateDonation.setOnClickListener {
            val intent = Intent(requireContext(), AskDonationsActivity::class.java)
            startActivity(intent)
        }

    }
}