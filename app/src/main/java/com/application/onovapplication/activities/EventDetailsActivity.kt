package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.ActivityDonationsNgoBinding
import com.application.onovapplication.databinding.ActivityEventDetailsBinding
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.CreateEventViewModel
import com.application.onovapplication.viewModels.EventDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class EventDetailsActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEventDetailsBinding

    val eventDtViewModel by lazy { ViewModelProvider(this).get(EventDetailViewModel::class.java) }
    lateinit var event:EventData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
      init()
        observeViewModel()
    }
fun init(){
    event = intent.getParcelableExtra<EventData>("data") as EventData
    Glide.with(this).load(BaseUrl.photoUrl +event.cover_image).apply(RequestOptions().placeholder(R.drawable.background_gradient_square)).into( binding.eventPhoto)
    binding.etDesc.text =event.description
    binding.tvEventStart.text =event.start_date
    binding.tvEventEnd.text =event.end_date
    binding.eventTitle.text =event.title
    binding.evPrice.text ="$"+event.price

    //tvScreenTitle.text = getString(R.string.event_details)
    binding.noOfEvent.text=eventDtViewModel.number.toString()
    eventDtViewModel.price=(event.price)!!.toInt()
    binding.payEventBtn.text="Proceed to Pay $"+event.price
    binding.noPeople.text=event.purchaseCount+" People are going"
}
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.payEventBtn -> {
                eventDtViewModel.buyEvent(this,userPreferences.getUserREf(),event.id.toString(), binding.noOfEvent.text.toString(), binding.payEventBtn.text.toString())

            }R.id.btnAdd -> {
            eventDtViewModel.addNumber()
            binding.noOfEvent.text=eventDtViewModel.number.toString()
            binding.payEventBtn.text="Proceed to Pay $"+eventDtViewModel.total.toString()
            }R.id.btnSubtract -> {
            eventDtViewModel.decNumber()
            binding.noOfEvent.text=eventDtViewModel.number.toString()
            binding.payEventBtn.text="Proceed to Pay $"+eventDtViewModel.total.toString()
            }
        }
    }

    private fun observeViewModel() {

        eventDtViewModel.successfullyBuyEvent.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (eventDtViewModel.status == "success") {
                        val intent = Intent(this, EventPaymentActivity::class.java)
                        intent.putExtra("eventId",event.id)
                        startActivity(intent)
                        finish()
                    } else {
                        setError(eventDtViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(eventDtViewModel.message)
            }

        })}
}