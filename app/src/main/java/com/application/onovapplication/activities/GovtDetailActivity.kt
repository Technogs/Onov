package com.application.onovapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityFeedDetailBinding
import com.application.onovapplication.databinding.ActivityGovtDetailBinding
import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.Member

class GovtDetailActivity : AppCompatActivity() {
    lateinit var members:Member
    private lateinit var binding: ActivityGovtDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGovtDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        members=    intent.getParcelableExtra<Member>("member") as Member
        binding.nameTxt.text=members.first_name+""+members.last_name
        binding.stateName.text=members.state
        binding.govtTrackId.text=members.govtrack_id
        binding.gender.text=members.gender
        binding.dobDate.text=members.date_of_birth
        binding.partyName.text=members.party
        binding.officeName.text=members.office
        binding.titleVl.text=members.title
        binding.phoneNo.text=members.phone
        binding.faxId.text=members.fax
        binding.teitterAc.text=members.twitter_account
        binding.fbAc.text=members.facebook_account
        binding.youtubeAc.text=members.youtube_account
//        name_txt.text=members.first_name
//        name_txt.text=members.first_name
    }
}