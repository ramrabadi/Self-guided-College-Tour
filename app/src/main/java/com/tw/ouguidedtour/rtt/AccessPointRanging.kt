package com.tw.ouguidedtour.rtt

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.rtt.RangingRequest
import android.net.wifi.rtt.RangingResult
import android.net.wifi.rtt.RangingResultCallback
import android.net.wifi.rtt.WifiRttManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class AccessPointRanging: AppCompatActivity() {

    private val SCAN_RESULT_EXTRA: String = "com.tw.ouguidedtour.extra.SCAN_RESULT"

    private lateinit var mScanResult: ScanResult

    private lateinit var mMac: String

    private var mNumberOfRangeRequests = 0
    private var mNumberOfSuccessfulRangeRequests = 0

    private var mSampleSize = 0

    private var mStatisticRangeHistoryEndIndex = 0
    private var mStatisticRangeHistory: ArrayList<Int>? = null

    private var mWifiRttManager: WifiRttManager? = null
    private var mRttRangingResultCallback: RttRangingResultCallback? = null


    private var mTimeBeforeNewRangingRequest: Int = 0

    val mRangeRequestDelayHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        mScanResult = intent.getParcelableExtra(SCAN_RESULT_EXTRA)!!

        if (mScanResult == null) {
            finish()
        }

        // Stores the mac address of th access point (i.e. The wifi router)
        mMac = mScanResult.BSSID

        mWifiRttManager = getSystemService(Context.WIFI_RTT_RANGING_SERVICE) as WifiRttManager
        mRttRangingResultCallback = RttRangingResultCallback()

        mStatisticRangeHistory = ArrayList()

        startRangingRequest()

    }

    override fun onDestroy() {
        super.onDestroy()
        remove_history()
    }

    private fun startRangingRequest() {

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            finish()
        }
        mNumberOfRangeRequests++
        val rangingRequest = RangingRequest.Builder().addAccessPoint(mScanResult).build()
        mRttRangingResultCallback?.let {
            mWifiRttManager!!.startRanging(
                rangingRequest, application.mainExecutor, it
            )
        }
    }

    // Calculates the mean distance I don't know if we will need or use this but it could be useful
    private fun getMeanDistance(): Float {
        var distanceSum = 0f
        for (distance in mStatisticRangeHistory!!) {
            distanceSum += distance.toFloat()
        }
        // !!.size is for if mStatisicRangeHistory == 0
        return distanceSum / mStatisticRangeHistory!!.size
    }

    // Adds the distance to that access points history, we can use this to tell if the user is moving or not with the getMeanDistance
    private fun addDistanceToHistory(distance: Int) {
        if (mStatisticRangeHistory!!.size >= mSampleSize) {
            if (mStatisticRangeHistoryEndIndex >= mSampleSize) {
                mStatisticRangeHistoryEndIndex = 0
            }
            mStatisticRangeHistory!![mStatisticRangeHistoryEndIndex] = distance
            mStatisticRangeHistoryEndIndex++
        } else {
            mStatisticRangeHistory!!.add(distance)
        }
    }

    private fun remove_history () {
        mSampleSize = 0
        mNumberOfSuccessfulRangeRequests = 0
        mNumberOfRangeRequests = 0
        mStatisticRangeHistoryEndIndex = 0
        mStatisticRangeHistory!!.clear()
    }
    // TODO
    private class RttRangingResultCallback() : RangingResultCallback() {
        private fun queueNextRangingRequest() {
        }

        override fun onRangingFailure(code: Int) {

        }

        override fun onRangingResults(list: List<RangingResult>) {

        }
    }
}