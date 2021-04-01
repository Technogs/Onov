package com.application.onovapplication.activities.common

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.onovapplication.R
import com.application.onovapplication.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseAppCompatActivity() {

    var donationStatus = ""
    var notificationStatus = ""
    var userRef = ""
    private val settingsViewModel by lazy {
        ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        userRef = userPreferences.getUserREf()

        settingsViewModel.getSettings(this, userRef)
        showDialog()

        tvScreenTitle.text = getString(R.string.settings)

        setSwitchEventListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        settingsViewModel.getSettingsSuccess.observe(this, Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {
//
                    if (settingsViewModel.getSettingsStatus == "success") {

                        donationStatus =
                            settingsViewModel.getSettingsResponse.settingsData!!.donationsVisible!!
                        notificationStatus =
                            settingsViewModel.getSettingsResponse.settingsData!!.notification!!

                        setSwitch(donationStatus, notificationStatus)

                    } else {
                        setError(settingsViewModel.message)
                    }

                } else {
                    setError(settingsViewModel.message)

                }
            }
        })


        settingsViewModel.successful.observe(this, Observer {

            dismissDialog()
            if (it) {
//
                if (settingsViewModel.status == "success") {
                    setError(settingsViewModel.message)
                } else {
                    setError(settingsViewModel.message)

                }
            } else {
                setError(settingsViewModel.message)

            }

        })
    }

    private fun setSwitch(donationStatus: String, notificationStatus: String) {
        switchDonation.isChecked = donationStatus != "0"
        switchNotification.isChecked = notificationStatus != "0"
    }

    private fun setSwitchEventListener() {
        switchNotification.setOnClickListener {
            notificationStatus = if (switchNotification.isChecked) {
                "1"
            } else {
                "0"
            }
            settingsViewModel.saveSettings(this, userRef, notificationStatus, donationStatus)
            showDialog()
        }



        switchDonation.setOnClickListener {
            donationStatus = if (switchDonation.isChecked) {
                "1"
            } else {
                "0"
            }
            settingsViewModel.saveSettings(this, userRef, notificationStatus, donationStatus)
            showDialog()
        }

    }
}