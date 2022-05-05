package com.application.onovapplication.activities.common

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityForgotPasswordOtpBinding
import com.application.onovapplication.viewModels.VerifyViewModel



class ForgotPasswordOtpActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val verifyViewModel by lazy {
        ViewModelProviders.of(this).get(VerifyViewModel::class.java)
    }
    var type: String? = null
    var role: String? = null
    var email: String? = null
    var otp_recieved: String? = null
    private lateinit var binding: ActivityForgotPasswordOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordOtpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab


       binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {

                otp_recieved = otp
                Toast.makeText(
                    this@ForgotPasswordOtpActivity,
                    "The OTP is $otp",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        observeViewModel()

        type = intent.getStringExtra("type")!!

        role = intent.getStringExtra("role")

        email = intent.getStringExtra("email")


        if (type == "verify") {
            incBinding.tvScreenTitle.text = getString(R.string.verify)
            otp_recieved = intent.getStringExtra("otp")

            binding.otpView.setOTP(otp_recieved!!)

        } else {
            incBinding.tvScreenTitle.text = getString(R.string.forgot_password_)

            verifyViewModel.forgetPassword(this, email!!)
            showDialog()
        }


    }

    private fun observeViewModel() {

        verifyViewModel.successfullyVerified.observe(this, Observer {
            dismissDialog()
            if (it) {

                if (type == "verify") {


                    val intent =
                        Intent(this@ForgotPasswordOtpActivity, HomeTabActivity::class.java)
                    intent.putExtra("role", role)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    val intent =
                        Intent(this@ForgotPasswordOtpActivity, ResetPasswordActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }
            }

        })


        verifyViewModel.otpSentSuccess.observe(this, Observer {

            dismissDialog()

            setError(verifyViewModel.message)

        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnForgotOtpContinue -> {

                if (otp_recieved != null) {
                    verifyViewModel.verifyOtp(this, email!!, otp_recieved!!)
                    showDialog()
                } else {
                    setError(getString(R.string.otp_valid_error))
                }
            }
        }
    }


}