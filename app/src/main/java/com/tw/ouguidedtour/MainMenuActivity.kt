package com.tw.ouguidedtour

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

class MainMenuActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        checkForPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            "Fine Location",
            ACCESS_FINE_LOCATION_RQ
        )
        checkForPermissions(
            android.Manifest.permission.CAMERA,
            "Camera",
            ACCESS_CAMERA_RQ
        )


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

    override fun onBackPressed() {
        super.onBackPressed();
        finishAffinity();
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
     




    /** Functions which check for permission and request permissions **/

    /**
     *  Function: checkForPermissions
     *
     *  Purpose: This checks if the given permission has been granted or not, if it has been granted it displays a Toast to the screen
     *           sawing that the permission has been granted if not then it calls the requestPermissions function from ActivityCompat,
     *           this is the function that will be called to check and if not already granted calls other functions to request the permissions.
     *
     *  Parameters:     permission: String, Exp(android.Manifest.permission.ACCESS_FINE_LOCATION) This is a permission which must be in the Manifest.xml file.
     *
     *                  name: String, This is a string value that is a name given to the Permission by the coder and will be used if the permission has not
     *                                  been given already.
     *
     *                  requestCode: Int, This is a integer value that is created by the coder it could be any value only used later to check which permission needs
     *                                     to be used to request the permission
     *
     *  Pre-condition:  Valid input
     *
     *  Post-condition: If the permission being checked is granted sends a toast to the screen. If not calls showWhyRequest which displays a prompt for why we are asking
     *                  for this permission to be granted.
     *
     */
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        when {
            ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale(permission) -> showWhyRequest(
                permission,
                name,
                requestCode
            )

            else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }


    /**
     *  Function: showWhyRequest
     *
     *  Purpose: Shows a dialog box in the middle of the screen telling the user why we are going to request location permissions
     *
     *  Parameters: permission: String, Exp(android.Manifest.permission.ACCESS_FINE_LOCATION) This is a permission which must be in the Manifest.xml file.
     *
     *              name: String, This is a string value that is a name given to the Permission by the coder and will be used if the permission has not
     *                            been given already.
     *
     *             requestCode: Int, This is a integer value that is created by the coder it could be any value only used later to check which permission needs
     *                               to be used to request the permission
     *
     *  Pre-condition: Valid input
     *
     *  Post-condition: Displays dialog box and then once the user hit the OK button then stack goes back to checkForPermissions and to the else statement
     *
     */
    private fun showWhyRequest(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        if (requestCode == ACCESS_FINE_LOCATION_RQ) {
            builder.apply {
                setMessage("Permission to access your Location to help you navigate you to the Stocker Center.")
                setTitle("Permission Required")
                setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this@MainMenuActivity,
                        arrayOf(permission),
                        requestCode
                    )
                }
            }

            val dialog = builder.create()

            dialog.show()
        } else if (requestCode == ACCESS_CAMERA_RQ) {
            builder.apply {
                setMessage("Permission to access your Camera is required to scan QR codes which are used to guide you through a Tour of Stocker.")
                setTitle("Permission Required")
                setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this@MainMenuActivity,
                        arrayOf(permission),
                        requestCode
                    )
                }
            }

            val dialog = builder.create()

            dialog.show()
        }

    }

    /**
     *  Function: override of onRequestPermissionResult
     *
     *  Purpose: Shows a dialog box in the middle of the screen telling the user why we are going to request location permissions
     *
     *  Parameters: context: this@MainActivity
     *
     *              permission: String, Exp(android.Manifest.permission.ACCESS_FINE_LOCATION) This is a permission which must be in the Manifest.xml file
     *
     *             grandResults: IntArray,
     *
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission denied", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }

        when (requestCode) {
            ACCESS_FINE_LOCATION_RQ -> innerCheck("Fine Location")
            ACCESS_CAMERA_RQ -> innerCheck("Camera")
        }
    }

    /** ----------------------------------------------------------------------- **/
}
