package com.application.onovapplication.activities.politicians

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.adapters.DebateRequestsAdapter
import com.application.onovapplication.adapters.DebateRequestsPoliticianAdapter
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityCreateLawBinding
import com.application.onovapplication.databinding.ActivityPoliticianDebateRequestBinding


class PoliticianDebateRequestActivity : AppCompatActivity() {
    private var debateRequestsAdapter: DebateRequestsPoliticianAdapter? = null
    private lateinit var binding: ActivityPoliticianDebateRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoliticianDebateRequestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab

        incBinding.tvScreenTitle.text = getString(R.string.debate_requests)

        debateRequestsAdapter = DebateRequestsPoliticianAdapter(this)
      binding.rvDebateRequestsPoliticians.adapter = debateRequestsAdapter

    }
}