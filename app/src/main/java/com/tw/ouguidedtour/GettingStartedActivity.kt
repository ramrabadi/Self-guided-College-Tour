package com.tw.ouguidedtour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button

class GettingStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        val i_understand_button: Button = findViewById(R.id.iunderstand)
        i_understand_button.setOnClickListener {
            val tutorialIntent = Intent(this, MainMenuActivity::class.java)
            startActivity(tutorialIntent)
            finish()
        }
    }
}