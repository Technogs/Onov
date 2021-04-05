package com.application.onovapplication.activities.citizens

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.politicians.RequestDebateActivity
import com.application.onovapplication.adapters.DebateRequestsAdapter
import kotlinx.android.synthetic.main.activity_debate_requests.*

class DebateRequestsActivity : BaseAppCompatActivity(), View.OnClickListener {

    private var debateRequestsAdapter: DebateRequestsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debate_requests)

        debateRequestsAdapter = DebateRequestsAdapter(this)
        rv_debate_requests.adapter = debateRequestsAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCreateDebate -> {

                val intent = Intent(this, RequestDebateActivity::class.java)
                startActivity(intent)
            }

        }
    }
}