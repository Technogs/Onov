package com.application.onovapplication.activities.lpa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.AnnouncementsAdapter
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_announcements.*

class AnnouncementsActivity : BaseAppCompatActivity() {
    var announcementsAdapter: AnnouncementsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcements)
        tvScreenTitle.text = getString(R.string.announcements)
      //  tvScreenTitleRight.text = getString(R.string.create_)

        announcementsAdapter = AnnouncementsAdapter(this)

        rv_announcements.adapter = announcementsAdapter
    }
}