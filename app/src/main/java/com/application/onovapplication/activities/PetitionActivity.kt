package com.application.onovapplication.activities

import android.content.Intent
import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.activities.common.CreateDonationActivity
import com.application.onovapplication.activities.common.StartPetition
import com.application.onovapplication.adapters.PetitionsAdapter
import kotlinx.android.synthetic.main.activity_petition.*
import kotlinx.android.synthetic.main.fragment_donations.*

class PetitionActivity : BaseAppCompatActivity() {

    private var petitionsAdapter: PetitionsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petition)

        petitionsAdapter = PetitionsAdapter(this)

        rv_petitions.adapter = petitionsAdapter

        btnCreatePetition.setOnClickListener {
            val intent = Intent(this, StartPetition::class.java)
            startActivity(intent)
        }
    }
}
