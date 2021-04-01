package com.application.onovapplication.activities.lpa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class CreateAnnouncementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_announcement)

        tvScreenTitle.text = getString(R.string.create_announcements)
        tvScreenTitleRight.text = getString(R.string.post)

    }
}