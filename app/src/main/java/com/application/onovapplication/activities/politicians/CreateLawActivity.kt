    package com.application.onovapplication.activities.politicians

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import kotlinx.android.synthetic.main.action_bar_layout_2.*

class CreateLawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_law)
        tvScreenTitle.text = getString(R.string.new_law)
        tvScreenTitleRight.text = getString(R.string.post)

    }
}