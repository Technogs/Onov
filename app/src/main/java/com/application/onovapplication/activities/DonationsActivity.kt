package com.application.onovapplication.activities

import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.DonationsAdapter
import kotlinx.android.synthetic.main.activity_donations.*

class DonationsActivity : BaseAppCompatActivity() {

    private var donationsAdapter: DonationsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations)

        donationsAdapter = DonationsAdapter(this)
        rv_donations.adapter = donationsAdapter

    }
}