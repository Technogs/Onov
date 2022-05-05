package com.application.onovapplication.activities.lpa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityCreateAnnouncementBinding

class CreateAnnouncementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAnnouncementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAnnouncementBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab
       incBinding.tvScreenTitle.text = getString(R.string.create_announcements)

    }
}