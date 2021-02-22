package com.tw.ouguidedtour

import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureActivity

class MainMenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val scanQRCode: Button = findViewById(R.id.QRCodeButton2)
        val mapButton: Button = findViewById(R.id.floorPlanButton)

        mapButton.setOnClickListener {
            val dataIntent = Intent(this, MainActivity::class.java)
            startActivity(dataIntent)


        }
        // Open Camera
        scanQRCode.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //startActivity(cameraIntent)
            val intentIntegrator = IntentIntegrator(this@MainMenuActivity)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.captureActivity = CaptureActivity::class.java
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            intentIntegrator.initiateScan()


        }
    }
        //Sends QR data to Database activity
        override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            //super.onActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    val s: String = result.contents;
                    val dataIntent = Intent(this, DataActivity::class.java)
                    dataIntent.putExtra("QRData", s)
                    startActivity(dataIntent)
                }
            } else {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }



}