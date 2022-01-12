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
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityAskDonationsBinding
import com.application.onovapplication.databinding.ActivityChangePasswordBinding
import com.application.onovapplication.viewModels.NewPasswordViewModel


class ChangePasswordActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityChangePasswordBinding

    private val newPasswordViewModel by lazy {
        ViewModelProvider(this).get(NewPasswordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.inc
        incBinding.tvScreenTitle.text = getString(R.string.change_password)

        observeViewModel()

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnChangePassword -> {

                when {
                    binding.edNewCHangedPassword.text.toString().trim().isEmpty() -> {
                        setError(getString(R.string.new_password_error))
                    }
                    binding.edNewCHangedPassword.text.toString().trim().length < 8 -> {

                        setError(getString(R.string.password_length_error))
                    }
                    else -> {
                        newPasswordViewModel.changePassword(
                            this,
                            userPreferences.getUserREf(),
                            binding.edOldPassword.text.toString().trim(),
                            binding.edNewCHangedPassword.text.toString().trim()
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