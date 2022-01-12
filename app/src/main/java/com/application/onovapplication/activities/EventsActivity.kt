package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.AskToAddActivity
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.CreateEventActivity
import com.application.onovapplication.activities.common.HomeTabActivity
import com.application.onovapplication.adapters.EventsAdapter
import com.application.onovapplication.adapters.ViewDebatesAdapter
import com.application.onovapplication.databinding.ActivityCommentBinding
import com.application.onovapplication.databinding.ActivityEventsBinding
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.EventModel
import com.application.onovapplication.model.FeedsData
import com.application.onovapplication.viewModels.EventViewModel
import com.application.onovapplication.viewModels.HomeViewModel

class EventsActivity : BaseAppCompatActivity(), View.OnClickListener ,EventsAdapter.OnEventClick{

    val eventViewModel by lazy { ViewModelProvider(this).get(EventViewModel::class.java) }
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java)}
    var eventsAdapter:EventsAdapter?  = null

    private lateinit var binding: ActivityEventsBinding
    var eventObj: EventData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_events)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        observeViewModel()

        eventViewModel.getEvent(this,userPreferences.getuserDetails()?.userRef.toString())
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btnCreateEvent->{
//                val intent = Intent(this , CreateEventActivity::class.java)
//            startActivity(intent)

                val intent = Intent(this, AskToAddActivity::class.java)
                intent.putExtra("activity", "event")
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {

        eventViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it != null) {
                if (it) {
                    if (eventViewModel.status == "success") {
                     setLayout(eventViewModel.eventResponse)

                    } else {
                        setError(eventViewModel.message)
                        finish()
                    }
                }
            } else {
                setError(eventViewModel.message)
            }

        })

        homeViewModel.successfulLike.observe(this, androidx.lifecycle.Observer {
            dismissDialog()
            if (it) {
                if (homeViewModel.status == "success") {
                    setError(homeViewModel.message)
                    if (homeViewModel.status == "success") {
                        if (homeViewModel.message == "Disliked successfully") {
                            eventObj?.dislike = true
                            eventObj?.like = false
                            if ((eventObj?.likeCount)!!.toInt() > 0 && (eventObj?.Liked == "1"))
                                eventObj?.likeCount =
                                    ((eventObj?.likeCount)!!.toInt() - 1).toString()
                            eventObj?.dislikeCount =
                                ((eventObj?.dislikeCount)!!.toInt() + 1).toString()
                            eventObj?.Liked = "0"
                            eventObj?.Disliked = "1"

                            binding.rvEvents.adapter!!.notifyDataSetChanged()
                        } else if (homeViewModel.message == "Liked successfully") {
                            eventObj?.like = true
                            eventObj?.dislike = false
                            eventObj?.likeCount = ((eventObj?.likeCount)!!.toInt() + 1).toString()
                            if ((eventObj?.dislikeCount)!!.toInt() > 0 && (eventObj?.Disliked == "1"))
                                eventObj?.dislikeCount =
                                    ((eventObj?.dislikeCount)!!.toInt() - 1).toString()
                            eventObj?.Liked = "1"
                            eventObj?.Disliked = "0"

                            binding.rvEvents.adapter!!.notifyDataSetChanged()

                        }

                    } else {
                       setError(homeViewModel.message)
                    }
                } else {
                    setError(homeViewModel.message)
                    finish()
                }
            } else {
                setError(homeViewModel.message)
            }
        })

    }

    fun setLayout(searchModel: EventModel) {
        eventsAdapter = EventsAdapter(this,searchModel.eventData,this)
        binding.rvEvents.adapter = eventsAdapter

    }

    override fun OnEventLikeClick(type: String, dataItem: EventData?) {
        eventObj=dataItem
        homeViewModel.likeFeed(this,userPreferences.getuserDetails()?.userRef.toString(),
            dataItem?.id.toString(),"event",type)
    }

    override fun OnEventShareClick(dataItem: EventData?) {
        eventObj=dataItem
        val intent= Intent(this, UsersActivity::class.java)
        intent.putExtra("event",dataItem)
        startActivity(intent)
    }

    override fun OnEventCommentClick(dataItem: EventData?) {
        eventObj=dataItem
        val intent= Intent(this, CommentActivity::class.java)
        intent.putExtra("event",dataItem)
        startActivity(intent)
    }
}