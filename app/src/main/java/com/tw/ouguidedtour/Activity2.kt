package com.tw.ouguidedtour

import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class Activity2 : AppCompatActivity() {

    var videoView: VideoView? = null
    var mediaControls: MediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        //init
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //Set
        bottomNavigationView.selectedItemId = R.id.VideoViewMenu
        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {startActivity(Intent(applicationContext, MainMenuActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.MapMenu -> {startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ScanQR -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivity(cameraIntent)
                    val intentIntegrator = IntentIntegrator(this@Activity2)
                    intentIntegrator.setBeepEnabled(false)
                    intentIntegrator.setCameraId(0)
                    intentIntegrator.captureActivity = CaptureActivity::class.java
                    intentIntegrator.setPrompt("SCAN")
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                    intentIntegrator.initiateScan()
                }
                R.id.FloorPlanMenu -> {startActivity(Intent(applicationContext, FloorPlan::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.VideoViewMenu -> {startActivity(Intent(applicationContext, Activity2::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        });


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