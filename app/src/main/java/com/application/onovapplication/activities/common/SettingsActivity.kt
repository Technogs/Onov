package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivitySearchFriendsBinding
import com.application.onovapplication.databinding.ActivitySettingsBinding
import com.application.onovapplication.viewModels.SettingsViewModel


class SettingsActivity : BaseAppCompatActivity(),View.OnClickListener {

    var donationStatus = ""
    var notificationStatus = ""
    var userRef = ""
    private val settingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding:ActionBarLayout2Binding=binding.ab
        userRef = userPreferences.getUserREf()

        settingsViewModel.getSettings(this, userRef)
        showDialog()

        incBinding.tvScreenTitle.text = getString(R.string.settings)

        setSwitchEventListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        settingsViewModel.getSettingsSuccess.observe(this, Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {

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
        binding.switchDonation.isChecked = donationStatus != "0"
        binding.switchNotification.isChecked = notificationStatus != "0"
    }

    private fun setSwitchEventListener() {
        binding.switchNotification.setOnClickListener {
            notificationStatus = if (binding.switchNotification.isChecked) {
                "1"
            } else {
                "0"
            }
            settingsViewModel.saveSettings(this, userRef, notificationStatus, donationStatus)
            showDialog()
        }



        binding.switchDonation.setOnClickListener {
            donationStatus = if (binding.switchDonation.isChecked) {
                "1"
            } else {
                "0"
            }
            settingsViewModel.saveSettings(this, userRef, notificationStatus, donationStatus)
            showDialog()
        }

    }

    override fun onClick(v: View?) {
     when(v?.id){
         R.id.emailEdit->{

         }
         R.id.phoneEdit->{

         }
     }
    }
}