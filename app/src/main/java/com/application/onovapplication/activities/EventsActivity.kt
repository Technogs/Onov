package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.CreateEventActivity
import com.application.onovapplication.adapters.EventsAdapter
import kotlinx.android.synthetic.main.activity_events.*

class EventsActivity : BaseAppCompatActivity(), View.OnClickListener {


    var eventsAdapter: EventsAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)


        eventsAdapter = EventsAdapter(this)
        rv_events.adapter = eventsAdapter

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btnCreateEvent->{
                val intent = Intent(this , CreateEventActivity::class.java)
            startActivity(intent)
            }
        }
    }
}