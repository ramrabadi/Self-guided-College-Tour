package com.tw.ouguidedtour

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.rtt.WifiRttManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val ACCESS_FINE_LOCATION_RQ = 101
    private val ACCESS_CAMERA_RQ = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("onCreate Called")


        checkForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "Fine Location", ACCESS_FINE_LOCATION_RQ)
        checkForPermissions(android.Manifest.permission.CAMERA, "Camera", ACCESS_CAMERA_RQ)
    }

    /** Lifecycle Methods **/

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")

        // Start getting GPS location if outside

        // Start getting Wifi RTT ranging if inside
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")

        // Resume playing video
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")

        // Pause video
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")

        // Stop getting GPS location

        // Stop getting Wifi RTT ranging
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
            shouldShowRequestPermissionRationale(permission) -> showWhyRequest(permission, name, requestCode)

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

        builder.apply {
            setMessage("Permission to access your $name is required help navigate you to the Stocker and through Stocker.")
            setTitle("Permission Required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }

        val dialog = builder.create()

        dialog.show()
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


}


