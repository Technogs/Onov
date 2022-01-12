package com.application.onovapplication.activities.common

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.application.onovapplication.R
import com.application.onovapplication.activities.DonationsActivity
import com.application.onovapplication.activities.EventsActivity

class AskToAddActivity : BaseAppCompatActivity(),View.OnClickListener {
    var activty:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_to_add)

        activty=intent.getStringExtra("activity").toString()

    }

    override fun onClick(v: View?) {
       when(v?.id){
          R.id.post_photo->{
              if(activty=="donation"){
                 donationActivity("photo")
              } else if(activty=="petition"){
                 petitionActivity("photo")
              }else if(activty=="event"){
                 eventActivity("photo")
              }

          }
          R.id.post_video->{

              if(activty=="donation"){
                  donationActivity("video")
              } else if(activty=="petition"){
                  petitionActivity("video")
              }else if(activty=="event"){
                  eventActivity("video")
              }

          }R.id.ivBackIcon->{
           val i= Intent(this, DonationsActivity::class.java)
           startActivity(i)
          }
       }
    }

    fun donationActivity(value:String){
        val i= Intent(this,AskDonationsActivity::class.java)
        i.putExtra("type",value)
        startActivity(i)
    }
    fun petitionActivity(value:String){
        val i= Intent(this,StartPetition::class.java)
        i.putExtra("type",value)
        startActivity(i)
    }
    fun eventActivity(value:String){
        val i= Intent(this, CreateEventActivity::class.java)
        i.putExtra("type",value)
        startActivity(i)
    }
}