package com.application.onovapplication.activities

import android.os.Bundle
import com.application.onovapplication.R
import com.application.onovapplication.activities.common.BaseAppCompatActivity
import com.application.onovapplication.adapters.PetitionsAdapter
import kotlinx.android.synthetic.main.activity_petition.*

class PetitionActivity : BaseAppCompatActivity() {

    private var petitionsAdapter: PetitionsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petition)

        petitionsAdapter = PetitionsAdapter(this)

        rv_petitions.adapter = petitionsAdapter
    }
}
