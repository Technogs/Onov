package com.application.onovapplication.activities.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.onovapplication.R
import com.application.onovapplication.adapters.ViewFollowersAdapter
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_view_followers.*

class ViewFollowersActivity : BaseAppCompatActivity() {

    var type: String? = null

    var viewFollowersAdapter: ViewFollowersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_followers)

        type = intent.getStringExtra("type")
        if (type == "following") {
            tvScreenTitle.text = getString(R.string.following)

        } else {
            tvScreenTitle.text = getString(R.string.followers)
        }


        viewFollowersAdapter = ViewFollowersAdapter(this, type.toString())
        rv_followers.adapter = viewFollowersAdapter


    }
}