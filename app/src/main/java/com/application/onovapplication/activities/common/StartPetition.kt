package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.application.onovapplication.R
import com.application.onovapplication.utils.CustomSpinnerAdapter
import kotlinx.android.synthetic.main.activity_start_petition.*

class StartPetition : BaseAppCompatActivity() {

    private val spinnerList =
        arrayOf("Select Petition Radius", "Local", "State", "Nationwide")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_petition)

        setSpinner()
    }

    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, spinnerList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spPetition.adapter = spinnerAdapter


        spPetition.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // selectedRole = parent?.getItemAtPosition(position).toString()
            }
        }
    }
}