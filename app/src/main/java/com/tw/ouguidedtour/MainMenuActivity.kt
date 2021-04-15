package com.tw.ouguidedtour

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import android.app.AlertDialog.Builder
import com.tw.ouguidedtour.Data.Tour

class MainMenuActivity: AppCompatActivity() {

    private var currentLocation: com.tw.ouguidedtour.Data.Location = com.tw.ouguidedtour.Data.Location()
    private var nextLocation: com.tw.ouguidedtour.Data.Location = com.tw.ouguidedtour.Data.Location()
    private var nextLocationId: String = "None"
    private var tour: Tour = Tour()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        if (checkPermission()) {
            val scanQRCode: Button = findViewById(R.id.QRCodeButton2)
            val mapButton: Button = findViewById(R.id.floorPlanButton)
            val tutorialButton: Button = findViewById(R.id.tutorialButton)
            val floorPlanButton: Button = findViewById(R.id.FloorPlanButton)
            mapButton.setOnClickListener {
                val dataIntent = Intent(this, MainActivity::class.java)
                startActivity(dataIntent)
            }
            tutorialButton.setOnClickListener {
                val tutorialIntent = Intent(this, TutorialActivity::class.java)
                startActivity(tutorialIntent)
            }
            floorPlanButton.setOnClickListener {
                val floorPlanIntent = Intent(this, FloorPlan::class.java)
                startActivity(floorPlanIntent)
            }
            scanQRCode.setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(cameraIntent)
                val intentIntegrator = IntentIntegrator(this@MainMenuActivity)
                intentIntegrator.setBeepEnabled(false)
                intentIntegrator.setCameraId(0)
                intentIntegrator.captureActivity = CaptureActivity::class.java
                intentIntegrator.setPrompt("SCAN")
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                intentIntegrator.initiateScan()
            }
        } else {
            requestPermission()
        }
    }


    //Sends QR data to Database activity
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val s: String = result.contents;
                val dataIntent = Intent(this, MainActivity::class.java)
                dataIntent.putExtra("id", s)
                startActivityForResult(dataIntent, requestCode)
            }
        } else {
            Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
     




    private fun checkPermission():Boolean {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED))
        {
            // Permission is not granted
            return false
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf<String>(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array <String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
                    val scanQRCode: Button = findViewById(R.id.QRCodeButton2)
                    val mapButton: Button = findViewById(R.id.floorPlanButton)
                    val tutorialButton: Button = findViewById(R.id.tutorialButton)
                    val floorPlanButton: Button = findViewById(R.id.FloorPlanButton)
                    mapButton.setOnClickListener {
                        val dataIntent = Intent(this, MainActivity::class.java)
                        startActivity(dataIntent)
                    }
                    tutorialButton.setOnClickListener {
                        val tutorialIntent = Intent(this, TutorialActivity::class.java)
                        startActivity(tutorialIntent)
                    }
                    floorPlanButton.setOnClickListener {
                        val floorPlanIntent = Intent(this, FloorPlan::class.java)
                        startActivity(floorPlanIntent)
                    }
                    scanQRCode.setOnClickListener {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivity(cameraIntent)
                        val intentIntegrator = IntentIntegrator(this@MainMenuActivity)
                        intentIntegrator.setBeepEnabled(false)
                        intentIntegrator.setCameraId(0)
                        intentIntegrator.captureActivity = CaptureActivity::class.java
                        intentIntegrator.setPrompt("SCAN")
                        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        intentIntegrator.initiateScan()
                    }
                }
                else {
                    Toast.makeText(applicationContext, "Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show()
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                        showMessageOKCancel("You need to allow access Camera Permissions to scan a QR code",
                            DialogInterface.OnClickListener {
                                    dialog, which-> requestPermission()
                            })
                    }

                }

        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainMenuActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    companion object {
        private
        const val PERMISSION_REQUEST_CODE = 200
    }
}
