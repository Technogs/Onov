package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityResetPasswordBinding
import com.application.onovapplication.viewModels.NewPasswordViewModel


class ResetPasswordActivity : BaseAppCompatActivity(), View.OnClickListener {
    private val newPasswordViewModel by lazy {
        ViewModelProvider(this).get(NewPasswordViewModel::class.java)
    }
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding = binding.ab
        incBinding.tvScreenTitle.text = getString(R.string.create_new_password)
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
                }else {
                    setError(newPasswordViewModel.message)
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

                    checkEmpty(binding.edOtp) -> {
                        setError("Enter Valid Otp")
                    }checkEmpty(binding.edNewPassword) -> {
                        setError(getString(R.string.new_password_error))
                    }
                    binding.edNewPassword.text.toString().length < 6 -> {
                        setError(getString(R.string.password_length_error))
                    }
                    checkEmpty(binding.edConfirmPassword) -> {
                        setError(getString(R.string.confirm_password_error))
                    }


                    binding.edNewPassword.text.toString() != binding.edConfirmPassword.text.toString() -> {
                        setError(getString(R.string.unmatch_password_error))

                    }
                    else -> {
                        if (intent.getStringExtra("email") != null)
                            newPasswordViewModel.createNewPassword(
                                this,
                                intent.getStringExtra("email").toString(),binding.edOtp.text.toString(),
                                binding.edNewPassword.text.toString()
                            )
                        else newPasswordViewModel.createNewPassword(
                            this, userPreferences.getuserDetails()?.email.toString(),binding.edOtp.text.toString(),
                            binding.edNewPassword.text.toString()
                        )

                        showDialog()
                    }

                }
            }
        }
    }


}