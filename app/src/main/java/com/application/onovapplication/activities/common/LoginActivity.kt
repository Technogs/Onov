package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.onovapplication.R
import com.application.onovapplication.databinding.ActivityLoginBinding
import com.application.onovapplication.utils.CustomSpinnerAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.live.kicktraders.viewModel.LoginViewModel

class LoginActivity : BaseAppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
//    private lateinit var token:String

    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    var token = ""
    private var selectedRole: String = ""


    private val rolesList =

        arrayOf("Select Role", "Citizens", "Politicians", "Organizations", "Entertainers", "LPA")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.application.onovapplication.databinding.ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.tvSignUp.setOnClickListener(this)
        observeViewModel()
        setSpinner()
        fbToken()
    }


    override fun onClick(clickEvent: View?) {
        when (clickEvent) {
            binding.tvSignUp -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            binding.btLogin -> {

                when {
                    checkEmpty( binding.etEmail) -> {
                        setError(getString(R.string.email_error))
                    }

                    !Patterns.EMAIL_ADDRESS.matcher( binding.etEmail.text.toString().trim())
                        .matches() -> {
                        setError(getString(R.string.invalid_email_error))
                    }

                    checkEmpty( binding.etPassword) -> {
                        setError(getString(R.string.password_error))
                    }

//                    selectedRole == "Select Role" -> {
//
//                        setError(getString(R.string.role_error))
//                    }

                    else -> {
                        loginViewModel.login(
                            this,
                            binding.etEmail.text.toString().trim(),
                            binding.etPassword.text.toString().trim(),
                             "Android",
                          token
                        )
                        showDialog()
                    }
                }
            }
        }
    }
    fun fbToken(){


        // 1
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                // 2
                if (!task.isSuccessful) {
                    Log.w("TAG token failed", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // 3
               token = task.result?.token.toString()

                // 4
                val msg = token
                Log.d("TAG token", msg.toString())
                userPreferences.saveUserToken(msg.toString())
//                Toast.makeText(this, userPreferences.getUserToken(), Toast.LENGTH_SHORT).show()

                //   Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
            }) }
    private fun observeViewModel() {

        loginViewModel.successful.observe(this, Observer {

            dismissDialog()
            if (it) {
                if (loginViewModel.status == "success") {

                    userPreferences.saveUserRef(loginViewModel.userInfo.userRef)
                    userPreferences.saveRole(loginViewModel.userInfo.role)
userPreferences.setUserDetails(loginViewModel.userInfo)
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
        binding.spinnerLogin.adapter = spinnerAdapter


        binding.spinnerLogin.onItemSelectedListener = object :
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