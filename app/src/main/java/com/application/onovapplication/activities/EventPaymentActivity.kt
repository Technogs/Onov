package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.EventAttendeesAdapter
import com.application.onovapplication.databinding.ActivityCommentBinding
import com.application.onovapplication.databinding.ActivityEventPaymentBinding
import com.application.onovapplication.model.AttendeesModel
import com.application.onovapplication.model.EventData
import com.application.onovapplication.viewModels.EventDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class EventPaymentActivity : BaseAppCompatActivity(), View.OnClickListener {
    val eventDtViewModel by lazy { ViewModelProvider(this).get(EventDetailViewModel::class.java) }
    lateinit var event: EventData
    lateinit var eventid: String
    lateinit var attendees: AttendeesModel
    private val mBottomSheetDialog by lazy {
        BottomSheetDialog(this, R.style.SheetDialog) }
    private lateinit var binding: ActivityEventPaymentBinding

    private var followersAdapter: EventAttendeesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
eventid=intent.getStringExtra("eventId").toString()
        eventDtViewModel.getAttendees(this,eventid)
        observeViewModel()
        binding.switchGoing.setOnCheckedChangeListener { _, isChecked ->
    if (isChecked) eventDtViewModel.eventPrivacy(this, userPreferences.getUserREf(), eventid, "1")
    else eventDtViewModel.eventPrivacy(this, userPreferences.getUserREf(), eventid, "0")


}

    }


        override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.peopleGoingBtn -> {
                showBottomSheet()
            }


        }
    }

    private fun showBottomSheet() {

        val sheetView: View =
            layoutInflater.inflate(R.layout.view_people_going, null)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
        var recyclerView=sheetView.findViewById<RecyclerView>(R.id.rv_people_going)
        var imageView=sheetView.findViewById<AppCompatImageView>(R.id.ivCancel)

//changed
      //  sheetView.let {
            followersAdapter = EventAttendeesAdapter(this,attendees.attendeeData)
        recyclerView.adapter = followersAdapter
      //  }



        imageView.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
    }


    private fun observeViewModel() {

        eventDtViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (eventDtViewModel.status == "success") {
                        //   setLayout(eventViewModel.eventResponse)
attendees=eventDtViewModel.data


                    } else {
                        setError(eventDtViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(eventDtViewModel.message)
            }

        })
        eventDtViewModel.successfulprivacy.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (eventDtViewModel.status == "success") {
                      //  Toast.makeText(this@EventPaymentActivity, eventDtViewModel.message, Toast.LENGTH_SHORT).show()


                    } else {
                        setError(eventDtViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(eventDtViewModel.message)
            }

        })
    }

}