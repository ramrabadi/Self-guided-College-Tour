package com.tw.ouguidedtour

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.squareup.picasso.Picasso


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

        /** Sets title of Location */
        title = intent.getStringExtra("name")

        /** The end of the Youtube URL */
        val videoUrl = intent.getStringExtra("videoUrl")

        /** Load image and set into item_image */
        //val imageURL = intent.getStringExtra("picture")
        //Picasso.get().load(imageURL).into(item_image)

        /** Set description to DescriptionView */
        val description = intent.getStringExtra("description")
        val descriptionView = findViewById<TextView>(R.id.DescriptionView)
        descriptionView.text = description


        val youTubePlayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)

        youTubePlayer.initialize("AIzaSyDmHD10feFC5D_FPP9OLkTzGAVJRWrLb80", object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideo(videoUrl)
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@TourActivity , "Video player Failed" , Toast.LENGTH_SHORT).show()
            }
        })

    }



}