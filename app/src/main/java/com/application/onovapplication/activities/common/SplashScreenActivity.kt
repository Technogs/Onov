package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.application.onovapplication.R
import com.application.onovapplication.prefs.PreferenceManager
import com.application.onovapplication.utils.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId


class SplashScreenActivity : BaseAppCompatActivity() {

    var startIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val bundle = intent.extras
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification

            Log.e("fsdfsdfsdf",bundle.get(Constants.USER_ID).toString())

        }





        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this@SplashScreenActivity,
            OnSuccessListener<Any> { instanceIdResult ->
                val newToken: String =FirebaseInstanceId.getInstance().getInstanceId().toString() //instanceIdResult.to
                Log.d("newToken", newToken)
//                userPreferences.saveUserToken(newToken)
//                Toast.makeText(this, userPreferences.getUserToken(), Toast.LENGTH_SHORT).show()
//                               sharedPreferences.setFCMToken(newToken);


            })
        if (checkGooglePlayServices()) {
            Log.w("google", "Device have google play services")

        } else {
            //You won't be able to send notifications to this device
            Log.w("google", "Device doesn't have google play services")
        }

        fbToken()


        startIntent = if (PreferenceManager(this).getUserREf().isNotEmpty()) {
            Intent(this@SplashScreenActivity, HomeTabActivity::class.java)
        } else {
            Intent(this@SplashScreenActivity, LoginActivity::class.java)
        }

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(startIntent)
            finish()
        }, 2000)
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
                    val token = task.result?.token

                    // 4
                    val msg = token
                    Log.d("TAG token", msg.toString())
                    userPreferences.saveUserToken(msg.toString())
//                Toast.makeText(this, userPreferences.getUserToken(), Toast.LENGTH_SHORT).show()

                }) }




    private fun checkGooglePlayServices(): Boolean {
        // 1
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        // 2
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(" checking error", "Error")
            // ask user to update google play services and manage the error.
            false
        } else {
            // 3
            Log.i("checking success", "Google play services updated")
            true
        }
    }


}