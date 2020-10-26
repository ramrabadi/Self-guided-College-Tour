package com.tw.ouguidedtour

import android.os.Bundle
import android.net.Uri
import android.widget.Toast
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class Activity2 : AppCompatActivity() {

    var videoView: VideoView? = null
    var mediaControls: MediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        videoView = findViewById<View>(R.id.videoView) as VideoView
        if (mediaControls == null) {
            mediaControls = MediaController(this)
            mediaControls!!.setAnchorView(this.videoView)
        }

        videoView!!.setMediaController(mediaControls)
        videoView!!.setVideoURI(Uri.parse("android.resource://"
                + packageName + "/" + R.raw.video))
        videoView!!.requestFocus()
        videoView!!.start()
        videoView!!.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video completed",
                Toast.LENGTH_LONG).show()
        }


        videoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(applicationContext, "An Error Occurred " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            false
        }

    }
}