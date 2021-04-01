package com.application.onovapplication.activities.common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.application.onovapplication.R
import com.application.onovapplication.prefs.PreferenceManager


class SplashScreenActivity : BaseAppCompatActivity() {

    var startIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

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

}