package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.live.kicktraders.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseAppCompatActivity(), View.OnClickListener {


    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    var token = "32324"
    private var selectedRole: String = ""


    private val rolesList =
        arrayOf("Select Role", "Citizens", "Politicians", "Organizations", "Entertainers", "lpa")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvSignUp.setOnClickListener(this)
        observeViewModel()
        setSpinner()
    }


    override fun onClick(clickEvent: View?) {
        when (clickEvent) {
            tvSignUp -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            btLogin -> {

                when {
                    checkEmpty(etEmail) -> {
                        setError(getString(R.string.email_error))
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim())
                        .matches() -> {
                        setError(getString(R.string.invalid_email_error))
                    }

                    checkEmpty(etPassword) -> {
                        setError(getString(R.string.password_error))
                    }

                    selectedRole == "Select Role" -> {
                        setError(getString(R.string.role_error))
                    }

                    else -> {
                        loginViewModel.login(
                            this,
                            etEmail.text.toString().trim(),
                            etPassword.text.toString().trim(),
                            "Android",
                            selectedRole,
                            token
                        )
                        showDialog()
                    }
                }
            }
        }
    }

    private fun observeViewModel() {

        loginViewModel.successful.observe(this, Observer {

            dismissDialog()
            if (it) {
                if (loginViewModel.status == "success") {

                    userPreferences.saveUserRef(loginViewModel.userInfo.userRef)
                    userPreferences.saveRole(loginViewModel.userInfo.role)

                    val intent = Intent(this@LoginActivity, HomeTabActivity::class.java)
                    intent.putExtra("role", loginViewModel.userInfo.role)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    setError(loginViewModel.message)
                }
            } else {
                setError(loginViewModel.message)
            }
        })
    }


    private fun setSpinner() {
        val spinnerAdapter = CustomSpinnerAdapter(
            this,  // Use our custom adapter
            R.layout.spinner_text, rolesList
        )


        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spinner_login.adapter = spinnerAdapter


        spinner_login.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedRole = parent?.getItemAtPosition(position).toString()
            }
        }
    }
}