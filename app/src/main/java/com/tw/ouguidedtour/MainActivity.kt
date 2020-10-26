package com.tw.ouguidedtour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent;
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }
        // Check for wifi permissions
        // Check for gps permissions


    }

    /** Lifecycle Methods **/

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")

        // Start getting GPS location if outside

        // Start getting Wifi RTT ranging if inside
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")

        // Resume playing video
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")

        // Pause video
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")

        // Stop getting GPS location

        // Stop getting Wifi RTT ranging
    }
}