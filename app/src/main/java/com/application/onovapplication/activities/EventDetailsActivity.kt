package com.application.onovapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class EventDetailsActivity : BaseAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        tvScreenTitle.text = getString(R.string.event_details)
    }
}