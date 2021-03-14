package com.tw.ouguidedtour.Data

class NavigationData {

    // Stores the latitude and longitude as well as the floor the location is on.
    private var lat: String
    private var long: String
    private var floor: String

    init {
        lat = ""
        long = ""
        floor = ""
    }

    fun setLat(temp: String) {
        lat = (temp)
    }
    fun setLong(temp: String) {
        long = (temp)
    }
    fun setFloor(temp: String) {
        floor = (temp)
    }

    fun getLat(): String {
        return lat
    }
    fun getLong(): String {
        return long
    }
    fun getFloor(): String {
        return floor
    }
}