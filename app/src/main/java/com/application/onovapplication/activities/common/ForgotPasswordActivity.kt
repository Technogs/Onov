package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActionBarLayout2Binding
import com.application.onovapplication.databinding.ActivityForgotPasswordBinding
import com.application.onovapplication.viewModels.VerifyViewModel

class ForgotPasswordActivity : BaseAppCompatActivity(),View.OnClickListener {
    private val verifyViewModel by lazy {
        ViewModelProviders.of(this).get(VerifyViewModel::class.java)
    }
    private lateinit var binding: ActivityForgotPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val incBinding: ActionBarLayout2Binding =binding.ab
        incBinding.tvScreenTitle.setText(R.string.forgot_password)

        observeViewModel()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnForgotOtpContinue->{
                if (binding.edEmail.text.toString()=="")setError("Enter a email")
              else {showDialog()
                    verifyViewModel.forgetPassword(this, binding.edEmail.text.toString())
                }

            }
        }
    }

    private fun observeViewModel() {

        verifyViewModel.otpSentSuccess.observe(this, Observer {

            dismissDialog()

            setError(verifyViewModel.message)
            if (it != null) {
                if (it) {
                    if (verifyViewModel.verifyStatus == "success") {
                        setError(verifyViewModel.message)
    val intent =
        Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
    intent.putExtra("email", binding.edEmail.text.toString())
    startActivity(intent)
    finish()
                    } else {
                        setError(verifyViewModel.message)
                    }
                }
            } else {
                setError(verifyViewModel.message)
            }


        })

    }

}