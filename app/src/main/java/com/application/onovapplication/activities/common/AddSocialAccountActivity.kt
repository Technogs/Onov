package com.application.onovapplication.activities.common

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityAddSocialAccountBinding
import com.application.onovapplication.viewModels.SettingsViewModel

class AddSocialAccountActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddSocialAccountBinding
    private val settingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSocialAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        settingsViewModel.getsocialaccount(
            this,
            userPreferences.getuserDetails()?.userRef.toString()
        )

        observeViewModel()
    }

    private fun observeViewModel() {
        settingsViewModel.socialAccountSuccess.observe(this, Observer {

            dismissDialog()
            if (it) {

                if (settingsViewModel.status == "success") {
                    setError(settingsViewModel.message)
                    finish()

                } else {
                    setError(settingsViewModel.message)
                    finish()
                }
            } else {
                setError(settingsViewModel.message)

            }

        })
        settingsViewModel.getSocialAccountSuccess.observe(this, Observer { successful ->
            dismissDialog()
            if (successful != null) {
                if (successful) {

                    if (settingsViewModel.status == "success") {

                        if (settingsViewModel.socialMediaResponse.socialData != null) {
                            binding.edFacebookName.setText(settingsViewModel.socialMediaResponse.socialData.facebook)//text=settingsViewModel.socialMediaResponse.socialData.facebook
                            binding.edInstagramName.setText(settingsViewModel.socialMediaResponse.socialData.instagram)
                            binding.edTwitterName.setText(settingsViewModel.socialMediaResponse.socialData.twitter)

                        }

                    } else {
                        setError(settingsViewModel.message)
                    }

                } else {
                    setError(settingsViewModel.message)

                }
            }
        })

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_social_account -> {
                if (binding.edInstagramName.text.toString() == "" || binding.edTwitterName.text.toString() == "" || binding.edFacebookName.text.toString() == "") {
                    setError("add atleast one account")
                } else {
                    showDialog()
                    settingsViewModel.addsocialmedia(
                        this,
                        userPreferences.getuserDetails()?.userRef.toString(),
                        binding.edInstagramName.text.toString(),
                        binding.edTwitterName.text.toString(),
                        binding.edFacebookName.text.toString()
                    )
                }
            }
        }
    }
}