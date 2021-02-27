package com.tw.ouguidedtour.rtt

import android.Manifest.permission
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
import com.tw.ouguidedtour.fspl.AccessPointData
import timber.log.Timber
import java.util.*


class AccessPointRangingResultsActivity : AppCompatActivity() {

    val SCAN_RESULT_EXTRA: String = "com.tw.ouguidedtour.rtt.extra.SCAN_RESULT"

    lateinit var data: MutableList<AccessPointRTTData>
    private var mScanResult: ScanResult? = null
    private var mBssid: String? = null
    private var mDistance: Float = 0.0f
    private var mNumberOfRangeRequests = 0
    private var mNumberOfSuccessfulRangeRequests = 0
    private var mMillisecondsDelayBeforeNewRangingRequest = 0

    private var mSampleSize = 1

    private var mDistanceRangeHistoryEndIndex = 0
    private var mDistanceRangeHistory: ArrayList<Int>? = null
    private var mLastMeanDistance = 0

    private var mWifiRttManager: WifiRttManager? = null
    private var mRttRangingResultCallback: RttRangingResultCallback? = null

    val mRangeRequestDelayHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        mScanResult = intent.getParcelableExtra(SCAN_RESULT_EXTRA)
        if (mScanResult == null) {
            finish()
        }

        mBssid = mScanResult!!.BSSID
        mWifiRttManager = getSystemService(WIFI_RTT_RANGING_SERVICE) as WifiRttManager
        mRttRangingResultCallback = RttRangingResultCallback()


        mDistanceRangeHistory = ArrayList()
        resetData()
        startRangingRequest()
    }

    private fun resetData() {
        mSampleSize = 0
        mMillisecondsDelayBeforeNewRangingRequest = 0
        mNumberOfSuccessfulRangeRequests = 0
        mNumberOfRangeRequests = 0
        mDistanceRangeHistoryEndIndex = 0
        mDistanceRangeHistory!!.clear()
    }

    private fun startRangingRequest() {
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            finish()
        }
        mNumberOfRangeRequests++
        val rangingRequest = RangingRequest.Builder().addAccessPoint(mScanResult!!).build()
        mWifiRttManager!!.startRanging(
            rangingRequest, application.mainExecutor, mRttRangingResultCallback!!
        )
    }

    private val meanDistance: Float
        get() {
            var distanceSum = 0f
            for (distance in mDistanceRangeHistory!!) {
                distanceSum += distance.toFloat()
            }
            return distanceSum / mDistanceRangeHistory!!.size
        }

    private fun addDistanceToHistory(distance: Int) {
        if (mDistanceRangeHistory!!.size >= mSampleSize) {
            if (mDistanceRangeHistoryEndIndex >= mSampleSize) {
                mDistanceRangeHistoryEndIndex = 0
            }
            mDistanceRangeHistory!![mDistanceRangeHistoryEndIndex] = distance
            mDistanceRangeHistoryEndIndex++
        } else {
            mDistanceRangeHistory!!.add(distance)
        }
    }

    // Class that handles callbacks for all RangingRequests and issues new RangingRequests.
    private inner class RttRangingResultCallback : RangingResultCallback() {
        private fun queueNextRangingRequest() {
            mRangeRequestDelayHandler.postDelayed(
                { startRangingRequest() },
                mMillisecondsDelayBeforeNewRangingRequest.toLong()
            )
        }

        override fun onRangingFailure(code: Int) {
            Timber.i(
                "onRangingFailure() code: $code"
            )
            queueNextRangingRequest()
        }

        override fun onRangingResults(list: List<RangingResult>) {
            Timber.i(
                "onRangingResults(): $list"
            )

            for(item in list) {
                if (item.status == RangingResult.STATUS_SUCCESS) {
                    mNumberOfSuccessfulRangeRequests++
                    mDistance = (item.distanceMm / 1000).toFloat()
                    val temp = meanDistance
                    addDistanceToHistory(item.distanceMm)

                    convertToObject(item.macAddress.toString(), mDistance, temp)
                } else if (item.status
                    == RangingResult.STATUS_RESPONDER_DOES_NOT_SUPPORT_IEEE80211MC
                ) {
                    Timber.i("RangingResult failed (AP doesn't support IEEE80211 MC.")
                } else {
                    Timber.i("RangingResult failed.")
                }

            }

            queueNextRangingRequest()
        }
    }


    private fun convertToObject(bssi: String, distance: Float, lastMean: Float) {
        lateinit var temp: AccessPointRTTData
        temp.BSSID = bssi
        temp.DISTANCE = distance
        temp.LASTMEAN = lastMean

        data.add(temp)
    }
}

