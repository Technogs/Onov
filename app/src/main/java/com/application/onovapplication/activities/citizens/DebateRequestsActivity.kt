package com.application.onovapplication.activities.citizens

import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.DebateRequestsAdapter
import kotlinx.android.synthetic.main.activity_debate_requests.*

class DebateRequestsActivity : BaseAppCompatActivity() {

    private var debateRequestsAdapter: DebateRequestsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debate_requests)

        debateRequestsAdapter = DebateRequestsAdapter(this)
        rv_debate_requests.adapter = debateRequestsAdapter
    }
}