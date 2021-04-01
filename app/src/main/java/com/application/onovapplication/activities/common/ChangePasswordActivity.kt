package com.application.onovapplication.activities.common

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.application.onovapplication.R
import com.application.onovapplication.viewModels.NewPasswordViewModel
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseAppCompatActivity(), View.OnClickListener {

    private val newPasswordViewModel by lazy {
        ViewModelProvider(this).get(NewPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        tvScreenTitle.text = getString(R.string.change_password)

        observeViewModel()

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnChangePassword -> {

                when {
                    edNewCHangedPassword.text.toString().trim().isEmpty() -> {
                        setError(getString(R.string.new_password_error))
                    }
                    edNewCHangedPassword.text.toString().trim().length < 8 -> {

                        setError(getString(R.string.password_length_error))
                    }
                    else -> {
                        newPasswordViewModel.changePassword(
                            this,
                            userPreferences.getUserREf(),
                            edOldPassword.text.toString().trim(),
                            edNewCHangedPassword.text.toString().trim()
                        )

                        showDialog()
                    }
                }

            }
        }
    }

    private fun observeViewModel() {
        newPasswordViewModel.successfullyChangedPassword.observe(this, Observer {
            if (it) {

                dismissDialog()

                if (newPasswordViewModel.status == "success") {

                    setError(newPasswordViewModel.message)

                    Handler(Looper.getMainLooper()).postDelayed({

                        finish()

                    }, 1500)
                } else {
                    setError(newPasswordViewModel.message)
                }

            } else {
                setError(newPasswordViewModel.message)
            }
        })
    }
}