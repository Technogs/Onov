package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class EventDetailsActivity : BaseAppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        tvScreenTitle.text = getString(R.string.event_details)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.payEventBtn -> {
                val intent = Intent(this, EventPaymentActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}