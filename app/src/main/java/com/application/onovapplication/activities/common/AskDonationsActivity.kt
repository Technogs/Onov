package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class AskDonationsActivity : BaseAppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_donations)
        tvScreenTitle.text = getString(R.string.ask_for_donations)

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnUploadImage -> {

            }
        }
    }
}