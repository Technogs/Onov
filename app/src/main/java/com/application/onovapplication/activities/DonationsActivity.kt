package com.application.onovapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.adapters.DonationsAdapter
import kotlinx.android.synthetic.main.activity_donations_ngo.*

class DonationsActivity : AppCompatActivity() {

    private var ngoDonationsAdapter1: DonationsAdapter? = null
    private var ngoDonationsAdapter2: DonationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations_ngo)

        ngoDonationsAdapter1 = DonationsAdapter(this)
        ngoDonationsAdapter2 = DonationsAdapter(this)

        rvSentDonationsNgo.adapter = ngoDonationsAdapter1
        rvReceivedDonationsNgo.adapter = ngoDonationsAdapter2


    }
}