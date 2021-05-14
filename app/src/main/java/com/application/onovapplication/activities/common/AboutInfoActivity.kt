package com.application.onovapplication.activities.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.application.onovapplication.R
import com.application.onovapplication.utils.CustomSpinnerAdapter
import kotlinx.android.synthetic.main.activity_about_info.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.spRegister

class AboutInfoActivity : BaseAppCompatActivity() {
    private val rolesList =
        arrayOf("Select Support", "Republican", "Democrat", "Independent")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_info)

        etText.setText("An “About Me,” also known as a blurb, is a short piece of writing that informs your reader about your professional background, key accomplishments, personal values and any brands you may be associated with.")

        setSpinner()
    }

    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, rolesList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spAbout.adapter = spinnerAdapter


        spAbout.onItemSelectedListener = object :
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
