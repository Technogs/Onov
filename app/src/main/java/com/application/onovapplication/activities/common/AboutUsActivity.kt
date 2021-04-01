package com.application.onovapplication.activities.common

import android.os.Bundle
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : BaseAppCompatActivity() {
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        type = intent.getStringExtra("type")

        when (type) {
            "about" -> {
                tvTitle.text = getString(R.string.about_us)
            }

            "privacy" -> {
                tvTitle.text = getString(R.string.privacy_policy)
            }

            else -> {
                tvTitle.text = getString(R.string.terms_amp_conditions)
            }
        }

    }
}