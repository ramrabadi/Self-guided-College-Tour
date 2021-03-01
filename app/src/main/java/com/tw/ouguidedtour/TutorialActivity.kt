package com.tw.ouguidedtour

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class TutorialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val scanTryIt: Button = findViewById(R.id.scan_button)
        scanTryIt.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(cameraIntent)
            val intentIntegrator = IntentIntegrator(this@TutorialActivity)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.captureActivity = CaptureActivity::class.java
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            intentIntegrator.initiateScan()
        }
        val watchVideoButton = findViewById < Button > (R.id.watch_video)
        watchVideoButton.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }
        val readAboutStop = findViewById < Button > (R.id.read_abt_stop)
        readAboutStop.setOnClickListener {
            val defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
            defaultBrowser.data = Uri.parse("https://www.ohio.edu/athens/bldgs/stocker.html")
            startActivity(defaultBrowser)
        }
        val openMap = findViewById < Button > (R.id.see_map_button)
        openMap.setOnClickListener {
            val dataIntent = Intent(this, MainActivity::class.java)
            startActivity(dataIntent)
        }
        val floorPlanButton: Button = findViewById(R.id.view_fp_button)
        floorPlanButton.setOnClickListener {
            // Display Floor Plan
            val intent = Intent(this, FloorPlan::class.java)
            startActivity(intent)
        }
    }
}