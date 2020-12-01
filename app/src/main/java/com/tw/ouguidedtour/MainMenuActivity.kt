package com.tw.ouguidedtour

import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    private val ACCESS_CAMERA_RQ = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val scanQRCode: Button = findViewById(R.id.QRCodeButton2)
        val mapButton: Button = findViewById(R.id.floorPlanButton)
        scanQRCode.setOnClickListener {
            val dataIntent = Intent(this, MainActivity::class.java)
            startActivity(dataIntent)


        }

        // Open Camera
        scanQRCode.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, ACCESS_CAMERA_RQ)
            val intentIntegrator = IntentIntegrator(this@MainMenuActivity)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
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
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    val dataIntent = Intent(this, DataActivity::class.java)
                    dataIntent.putExtra("QRData", result.contents)
                    startActivity(dataIntent)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }



}