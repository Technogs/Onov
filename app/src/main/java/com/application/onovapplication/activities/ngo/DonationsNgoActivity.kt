package com.application.onovapplication.activities.ngo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.adapters.NgoDonationsAdapter
import kotlinx.android.synthetic.main.activity_donations_ngo.*

class DonationsNgoActivity : AppCompatActivity() {

    private var ngoDonationsAdapter1: NgoDonationsAdapter? = null
    private var ngoDonationsAdapter2: NgoDonationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations_ngo)

        ngoDonationsAdapter1 = NgoDonationsAdapter(this)
        ngoDonationsAdapter2 = NgoDonationsAdapter(this)

        rvSentDonationsNgo.adapter = ngoDonationsAdapter1
        rvReceivedDonationsNgo.adapter = ngoDonationsAdapter2


    }
}