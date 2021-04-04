package com.tw.ouguidedtour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000 // timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            // start main menu activity once timer is finished
            startActivity(Intent(this, GettingStartedActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}