package com.tw.ouguidedtour.fspl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import kotlin.math.log10
import kotlin.math.pow

class mWifiManager: AppCompatActivity() {

    private lateinit var mwifiManager: WifiManager
    private lateinit var mwifiInfo: WifiInfo
    lateinit var results: List<ScanResult>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if (!mwifiManager.isWifiEnabled) {
            mwifiManager.isWifiEnabled = true
        }
        setupWifiManager(applicationContext, context = this)
        startScanner()
    }

    private val wifiScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }

    fun setupWifiManager(applicationContext: Context, context: Context) {
        mwifiManager =
            applicationContext.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {

            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)
        }
    }

    private fun scanSuccess() {
        results = mwifiManager.scanResults

        mainWork(results)
    }

    private fun scanFailure() {
        results = mwifiManager.scanResults
    }

    private fun startScanner(attachHandler: Boolean = true) {
        if (attachHandler) {
            val handler = Handler()
            val runnableCode = object : Runnable {
                override fun run() {
                    mwifiManager.startScan()
                    handler.postDelayed(this, 10000)
                }
            }
            handler.post(runnableCode)
        } else {
            mwifiManager.startScan()
        }
    }

    //```````````````````````````
    //  Takes the ScanResults list and puts every access point into an object with the Bssid value and the distance
    //```````````````````````````
    fun mainWork(info: List<ScanResult>): List<AccessPointData> {
        lateinit var data: MutableList<AccessPointData>

        for (item in info) {
            lateinit var temp: AccessPointData
            temp.BSSID = item.BSSID
            temp.FREQUENCY = item.frequency
            temp.LEVEL = item.level
            temp.DISTANCE = calculateDistance(temp.FREQUENCY, temp.LEVEL)
            data.add(temp)
        }

        return data
    }

    //``````````````````````
    // calculateSignalLevel is deprecated for versions older than 30 SDK, work around needs to been found
    //
    //  We might not need this, unless the phone is on an older SDK
    //``````````````````````
    private fun grabSignalLevel(signal: Int): Int {
        val signalLevel: Int
//        if (Build.VERSION.SDK_INT >= 30) {
//            signalLevel = mwifiManager.calculateSignalLevel(signal)
//        } else {
//            signalLevel = mwifiManager.calculateSignalLevel(signal, 5)
//        }
        if (Build.VERSION.SDK_INT >= 30) {
            signalLevel = mwifiManager.calculateSignalLevel(signal)
        } else {
            signalLevel = 0
        }

        return signalLevel
    }

    //````````````````````````````
    // Purpose: Free space path loss, finds the distance from an access point to the users device
    //
    // "27.55" is a constant that we use because we are using MHz for freq and meters for distance
    //````````````````````````````
    private fun calculateDistance(freq: Int, sigLev: Int): Float {
        val x = (27.55 - (20 * log10(freq.toDouble())) + (sigLev / 20))
        val output: Double = 10.0.pow(x)

        return output.toFloat()
    }
}