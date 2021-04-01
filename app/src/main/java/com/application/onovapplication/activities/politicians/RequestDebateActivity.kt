package com.application.onovapplication.activities.politicians

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class RequestDebateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_debate)
        tvScreenTitle.text = getString(R.string.request_debate)
        tvScreenTitleRight.text = getString(R.string.request)
    }
}