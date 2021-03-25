package com.tw.ouguidedtour

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class TourActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)

        val button = findViewById<Button>(R.id.go_to_next_location)
        button.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    override fun onResume() {
        super.onResume()

        displayLocation()
    }
    
    private fun displayLocation() {

        val intent = intent

        title = intent.getStringExtra("name")
        val videoUrl = intent.getStringExtra("videoUrl")
        val description = intent.getStringExtra("description")
        val videoView : VideoView = findViewById(R.id.TourVideoView)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setVideoPath(videoUrl)
        videoView.start()
        videoView.setMediaController(mediaController)

        val descriptionView = findViewById<TextView>(R.id.DescriptionView)
        descriptionView.text = description

    }

}