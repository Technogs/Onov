package com.application.onovapplication.activities.lpa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.AnnouncementsAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityAnnouncementsBinding
import com.application.onovapplication.databinding.ActivityViewFollowersBinding


class AnnouncementsActivity : BaseAppCompatActivity() {
    var announcementsAdapter: AnnouncementsAdapter? = null
    private lateinit var binding: ActivityAnnouncementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding:ActionBarLayout2Binding=binding.ab
       incBinding.tvScreenTitle.text = getString(R.string.announcements)
      //  tvScreenTitleRight.text = getString(R.string.create_)

        announcementsAdapter = AnnouncementsAdapter(this)

      binding.rvAnnouncements.adapter = announcementsAdapter
    }
}