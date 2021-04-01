package com.tw.ouguidedtour.Data

import android.content.res.AssetManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class Tour: AppCompatActivity() {

    // Id of the Location, never seen or used by the user
    private var id: String

    // The name of the Tour, (i.e., the Department the tour is for)
    private var name: String

    // Number of Stops in the tour
    private var stops: Int = 0

    // Number of Stops Visited
    private var stops_visited: Int = 0

    // Holds an array of Location Objects
    private var tour_stops: MutableList<Location> = mutableListOf()

    private var tour_stops_visited: MutableList<Boolean> = mutableListOf()

    init {
        id = "None"
        name = ""
        stops = 0
        stops_visited = 0
    }

    // input = the id of the location just scanned, should only be used once per tour
    fun get_tour_id(input: String, fileName: String, assets: AssetManager): String {


        val obj = JSONObject(getAssetJsonData(assets, fileName))
        lateinit var output: String

        // Holds and array of tours
        val toursArray: JSONArray = obj.getJSONArray("Tours")

        for (i in 0 until toursArray.length()) {

            // Holds first tour
            val temp = toursArray.getJSONObject(i)

            // Holds array of locations from "temp" tour
            val locationsArray = temp.getJSONArray("Locations")
            for (j in 0 until locationsArray.length()) {
                val location = locationsArray.getJSONObject(j)
                if (input == location.getString("id")) {
                    output = location.getString("tour_id")
                    return output
                }
            }
        }
        // This is for if the id of the location was not found
        output = "Error"
        return output
    }

    fun load_list_of_stops(tour: Tour, qr_string: String, fileName: String, assets: AssetManager) {
        val obj = JSONObject(getAssetJsonData(assets, fileName))

        // The tour id
        val input = get_tour_id(qr_string, fileName, assets)

        if (input == "Error") {
            //Timber.e("Could not find Location in File")
            return
        }

        // Holds and array of tours
        val toursArray: JSONArray = obj.getJSONArray("Tours")

        for (i in 0 until toursArray.length()) {
            // Holds tour from array
            val temp = toursArray.getJSONObject(i)

            if (tour.id == temp.getString("id")) {
                tour.name = temp.getString("name")
                tour.stops = temp.getInt("stops")

                val locationsArray = temp.getJSONArray("Locations")

                for (j in 0 until locationsArray.length()) {
                    var output: Location = Location()
                    val location = locationsArray.getJSONObject(j)
                    output.setId(location.getString("id"))
                    output.setName(location.getString("name"))
                    output.setNextLocationId(location.getString("next_location_id"))
                    output.setVideoUrl(location.getString("video_url"))
                    output.setDescription(location.getString("description"))
                    output.setIdOfTour((location.getString("tour_id")))
                    /*
                    val picturesArray = location.getJSONArray("pictures")
                    for (k in 0 until picturesArray.length()) {
                        val image = picturesArray.getJSONObject(k)
                        output.setPicture(image.getString("image"))
                    }
                    val tempNavigationData:NavigationData = NavigationData()

                    tempNavigationData.setLat((location.getDouble("lat")))
                    tempNavigationData.setLong((location.getDouble("long")))
                    tempNavigationData.setFloor((location.getInt("floor")))

                    output.setNavigationData(tempNavigationData)
                    */
                    tour.tour_stops.add(output)
                    tour.tour_stops_visited.add(false)
                }
            }
        }
    }

    // Loads and turns a .json file into a single string which can be parsed
    fun getAssetJsonData(assets: AssetManager, fileName: String): String {
        val json: String
        json = try {
            val `is`: InputStream = assets.open(fileName)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace().toString()
        }
        return json
    }

    // variable Setters
    fun setId(temp: String) {
        id = temp
    }
    fun setName(temp: String) {
        name = temp
    }
    fun setStops(temp: Int) {
        stops = temp
    }
    fun addStopVisited() {
        stops_visited += 1
    }
    // Change a location from not visited to visited
    fun setToursStopVisited(tour: Tour, temp: String) {
        var x = 0
        val list = tour.getTourStops()
        val visited = tour.getTourStopsVisited()
        for (i in 0 until list.size) {
            if (list[i].getId() == temp && !visited[i]) {
                tour_stops_visited[i] = true
            }
        }
    }

    // Variable getters
    fun getId(): String {
        return id
    }
    fun getName(): String {
        return name
    }
    fun getStops(): Int {
        return stops
    }
    fun getStopsVisited(): Int {
        return  stops_visited
    }
    fun getTourStops(): MutableList<Location> {
        return tour_stops;
    }
    fun getTourStopsVisited(): MutableList<Boolean> {
        return tour_stops_visited;
    }
    // Returns a Location from the tour_stops, returns a blank location with the name "Error" or "Error Already Visited Location"
    fun getLocation(tour: Tour, input: String): Location {
        var x: Location = Location()
        val list = tour.getTourStops()
        val visited = tour.getTourStopsVisited()
        for (i in 0 until list.size) {
            if (list[i].getId() == input && !visited[i]) {
                x = list[i]
            }
        }

        return x
    }

    fun beenToEveryLocation(tour: Tour): Boolean {
        return tour.getStops() == tour.getStopsVisited()
    }

    fun findNextUnvisitedLocation(tour: Tour): String {
        val x: MutableList<Location> = tour.getTourStops()
        val y: MutableList<Boolean> = tour.getTourStopsVisited()
        lateinit var output: String
        for ((i, j) in x.withIndex()) {
            if (!y[i]) {
                output = x[i].getId()
            }
        }
        return output
    }
}