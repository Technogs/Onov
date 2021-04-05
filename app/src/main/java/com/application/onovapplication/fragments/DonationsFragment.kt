package com.application.onovapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.CreateDonationActivity
import com.application.onovapplication.adapters.DonationsAdapter
import kotlinx.android.synthetic.main.fragment_donations.*


class DonationsFragment : Fragment() {

    private var donationsAdapter: DonationsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donationsAdapter = DonationsAdapter(requireContext())
        rv_donations_fragment.adapter = donationsAdapter

        btnCreateDonation.setOnClickListener {
            val intent = Intent(requireContext(), CreateDonationActivity::class.java)
            startActivity(intent)
        }
    }
}