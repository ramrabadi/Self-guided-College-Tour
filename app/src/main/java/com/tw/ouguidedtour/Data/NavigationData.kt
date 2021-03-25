package com.tw.ouguidedtour.Data

class NavigationData {

    // Stores the latitude and longitude as well as the floor the location is on.
    private var lat: Double
    private var long: Double
    private var floor: Int

    init {
        lat = 0.0
        long = 0.0
        floor = 0
    }

    fun setLat(temp: Double) {
        lat = (temp)
    }
    fun setLong(temp: Double) {
        long = (temp)
    }
    fun setFloor(temp: Int) {
        floor = (temp)
    }

    fun getLat(): Double {
        return lat
    }
    fun getLong(): Double {
        return long
    }
    fun getFloor(): Int {
        return floor
    }
}