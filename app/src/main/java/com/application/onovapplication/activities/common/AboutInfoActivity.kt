package com.application.onovapplication.activities.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.application.onovapplication.R
import com.application.onovapplication.model.UserInfo
import com.application.onovapplication.utils.CustomSpinnerAdapter
import kotlinx.android.synthetic.main.activity_about_info.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.spRegister

class AboutInfoActivity : BaseAppCompatActivity() {
    private val rolesList =
        arrayOf("Select Support", "Republican", "Democrat", "Independent")

    var userInfo: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_info)

        userInfo = intent.getParcelableExtra("user")

        // etText.setText("An “About Me,” also known as a blurb, is a short piece of writing that informs your reader about your professional background, key accomplishments, personal values and any brands you may be associated with.")
        setSpinner()

        setData()

    }

    private fun setData() {
        etText.setText(userInfo?.about)
        websiteLink.setText(userInfo?.webUrl)


        for (i in rolesList.indices) {
            if (rolesList[i] == userInfo?.supporter) {
                spAbout.setSelection(i)
            }
        }

        roleValue.text = userInfo?.role
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
