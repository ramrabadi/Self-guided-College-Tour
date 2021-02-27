package com.tw.ouguidedtour.database


class RttNode {

    // The Rssi value of the noe
    private var rssi: String = "0"

    // The distance from the users phone to the node
    private var distance: Float = 0.0f


    // Creates a object which is set with the rssi value of the noe and then the distance.
    fun create (rssi: String, distance: Float) {
        // Makes sure rssi receives a value
        if (rssi != "")
        {
            this.rssi = rssi
        }

        // Makes sure the distance is greater than 0
        if (distance > 0)
        {
            this.distance = distance
        }
    }

    // sets value of rssi
    fun setRssi(rssi: String) {
        this.rssi = rssi
    }

    // sets value of distance
    fun setDistance(distance: Float) {
        this.distance = distance
    }

    // returns the rssi value
    fun getRssi(): String {
        return rssi
    }

    // returns the distance
    fun getDistance(): Float {
        return distance
    }
}