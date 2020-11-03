package com.tw.ouguidedtour.database

class RttNode {

    // The Rssi value of the noe
    private var rssi: String = ""

    // The distance from the users phone to the node
    private var distance: Int = 0


    // Creates a object which is set with the rssi value of the noe and then the distance.
    fun create (rssi: String, distance: Int) {
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

    // returns the rssi value
    fun getRssi(): String {
        return rssi
    }

    // returns the distance
    fun getDistance(): Int {
        return distance
    }
}