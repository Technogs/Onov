package com.application.onovapplication.activities.politicians

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.adapters.DebateRequestsAdapter
import com.application.onovapplication.adapters.DebateRequestsPoliticianAdapter
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_politician_debate_request.*

class PoliticianDebateRequestActivity : AppCompatActivity() {
    private var debateRequestsAdapter: DebateRequestsPoliticianAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_politician_debate_request)

        tvScreenTitle.text = getString(R.string.debate_requests)

        debateRequestsAdapter = DebateRequestsPoliticianAdapter(this)
        rv_debate_requests_politicians.adapter = debateRequestsAdapter

    }
}