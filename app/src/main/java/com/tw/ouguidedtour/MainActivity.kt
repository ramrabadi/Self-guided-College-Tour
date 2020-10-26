package com.tw.ouguidedtour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

class MainActivity : AppCompatActivity() {

    private var locationManager : LocationManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("onCreate Called")

        // Check for wifi permissions
        // Check for gps permissions

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?


    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //LONGITUDE = location.longitude
            //LATITUDE = location.latitude
            //sets whatever var will hold lat & long to value
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}

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
}