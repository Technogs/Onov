package com.application.onovapplication.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.databinding.ActivityEventDetailsBinding
import com.application.onovapplication.model.EventData
import com.application.onovapplication.repository.BaseUrl
import com.application.onovapplication.viewModels.EventDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ui.SimpleExoPlayerView



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
    if (event.cover_image!="") {
        binding.idExoPlayerVIew.visibility=View.GONE
        binding.eventPhoto.visibility=View.VISIBLE
        Glide.with(this).load(BaseUrl.photoUrl + event.cover_image)
            .apply(RequestOptions().placeholder(R.drawable.background_gradient_square))
            .into(binding.eventPhoto)
    }  else if (event.ent_video!="") {
        binding.idExoPlayerVIew.visibility=View.VISIBLE
        binding.eventPhoto.visibility=View.GONE
        exoplayer(BaseUrl.photoUrl + event.ent_video,binding.idExoPlayerVIew)
    }
    binding.etDesc.text =event.description
    binding.tvEventStart.text = convertDateFormat(event.start_date, "yyyy-MM-dd", "MMM dd,yyyy")
    binding.tvEventEnd.text =convertDateFormat(event.end_date, "yyyy-MM-dd", "MMM dd,yyyy")
    binding.eventTitle.text =event.title
    binding.evPrice.text ="$"+event.price

    binding.noOfEvent.text=eventDtViewModel.number.toString()
    eventDtViewModel.price=(event.price)!!.toInt()
    binding.payEventBtn.text="Proceed to Pay $"+event.price
    binding.noPeople.text=event.purchaseCount+" People are going"
}

    private fun showDialog(mdData: EventData) {

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val ivFeed = dialog.findViewById(R.id.ivFeed) as ImageView
        val cross = dialog.findViewById(R.id.close) as ImageView
        val vvFeed = dialog.findViewById(R.id.vvFeed) as VideoView

        val idExoPlayerVIew = dialog.findViewById(R.id.idExoPlayerVIew) as SimpleExoPlayerView
        val wbFeed = dialog.findViewById(R.id.wbFeed) as WebView
        cross.setOnClickListener { dialog.dismiss() }
//            when (mdData.fileType) {
//                "document" -> {
//                    wbFeed.visibility = View.VISIBLE
//                    vvFeed.visibility = View.GONE
//                    idExoPlayerVIew.visibility = View.GONE
//
//                    ivFeed.visibility = View.GONE
//                    wbFeed.settings.javaScriptEnabled = true
//
//                    wbFeed.settings.javaScriptCanOpenWindowsAutomatically = true
//                    wbFeed.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//                    wbFeed.loadUrl(
//                        "https://docs.google.com/gview?embedded=true&url=".plus(
//                            BaseUrl.photoUrl + mdData.filePath
//                        )
//                    )
//                    //     Log.e("PRACHI","https://docs.google.com/gview?embedded=true&url=".plus(BaseUrl.photoUrl + feedData.filePath))
//
//                }
//
//                "video" -> {
////                videoPosition = adapterPosition
////                Log.e("ViewDebate", "posiition=" + videoPosition)
//                    //here position
//                    wbFeed.visibility = View.GONE
//                    //   binding.vvFeed.visibility = View.VISIBLE
//                    idExoPlayerVIew.visibility = View.VISIBLE
//                    ivFeed.visibility = View.GONE
//
//                    exoplayer(BaseUrl.photoUrl + mdData.filePath)
//                }
//
//                "photo" -> {
        wbFeed.visibility = View.GONE
        vvFeed.visibility = View.GONE
        idExoPlayerVIew.visibility = View.GONE
        ivFeed.visibility = View.VISIBLE
        Glide.with(this).load(BaseUrl.photoUrl + mdData.cover_image).into(ivFeed)

//                }
//            }
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog?.cancel()
                return@OnKeyListener true

            }
            false
        })
        dialog.show()

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
            }R.id.event_photo -> {
           showDialog(event)
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