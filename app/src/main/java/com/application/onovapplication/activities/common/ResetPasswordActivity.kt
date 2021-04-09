package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.viewModels.NewPasswordViewModel
import kotlinx.android.synthetic.main.action_bar_layout_2.*
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val newPasswordViewModel by lazy {
        ViewModelProvider(this).get(NewPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        tvScreenTitle.text = getString(R.string.reset_password)
        observeViewModel()
    }


    private fun observeViewModel() {
        newPasswordViewModel.successful.observe(this, Observer {
            dismissDialog()
            if (it) {

                if (newPasswordViewModel.status == "success") {

                    setError(newPasswordViewModel.message)

                    Handler(Looper.getMainLooper()).postDelayed({

                        val intent =
                            Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                    }, 1500)
                }
            } else {
                setError(newPasswordViewModel.message)
            }
        })
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.btnResetPassword -> {
                when {

                    checkEmpty(edNewPassword) -> {
                        setError(getString(R.string.new_password_error))
                    }

                    checkEmpty(edConfirmPassword) -> {
                        setError(getString(R.string.confirm_password_error))
                    }

                    edNewPassword.text.toString().length < 8 -> {
                        setError(getString(R.string.password_length_error))
                    }

                    edNewPassword.text.toString() != edConfirmPassword.text.toString() -> {
                        setError(getString(R.string.unmatch_password_error))

                    }
                    else -> {

                        newPasswordViewModel.createNewPassword(
                            this,
                            intent.getStringExtra("mobile")!!,
                            edNewPassword.text.toString()
                        )

                        showDialog()
                    }

                }
            }
        }
    }


}