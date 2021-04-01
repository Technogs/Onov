package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class CreateEventActivity : BaseAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)


        tvScreenTitle.text = getString(R.string.create_event)
        tvScreenTitleRight.text = getString(R.string.post)
    }




}