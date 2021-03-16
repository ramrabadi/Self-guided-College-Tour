package com.tw.ouguidedtour

import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.tw.ouguidedtour.Data.Location
import com.tw.ouguidedtour.Data.Tour


class TourActivity: AppCompatActivity() {

    private var tour: Tour = Tour()
    private lateinit var currentLocation: Location
    private lateinit var nextLocation: Location
    private lateinit var nextLocationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)

        val button = findViewById<Button>(R.id.go_to_next_location)
        button.setOnClickListener {
            sendToNewPath()
        }

        // Grabs the first location found
        val intent = intent
        var input: String? = intent.getStringExtra("id")

        if (tour.getId() != "" || tour.getId() != "Error") {
            // Finds the tour Id of the location currently at, should only be used for the initial location
            tour.setId(tour.get_tour_id(input!!, "Tours.json", assets))
        }

        // Load the data for the entire tour
        tour.load_list_of_stops(tour, input!!, "Tour.json", assets)

        nextLocation = tour.getLocation(tour, currentLocation.getNextLocationId())
        nextLocationId = nextLocation.getId()

        if (nextLocationId == "") {
            if (!beenToEveryLocation() && nextLocationId != "Error"){
                findNextUnvisitedLocation()

                input = nextLocationId
            }
        }

        displayLocation(tour, input)

    }

    private fun displayLocation(tour: Tour, input: String) {

        currentLocation = tour.getLocation(tour, input)
        title = currentLocation.getName();
        val videoView : VideoView = findViewById(R.id.TourVideoView)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setVideoPath(currentLocation.getVideoUrl())
        videoView.start()
        videoView.setMediaController(mediaController)

        val descriptionView = findViewById<TextView>(R.id.DescriptionView)
        descriptionView.text = currentLocation.getDescription()

    }


    private fun sendToNewPath() {
        val tempNavigationData = nextLocation.getNavData()

        val intent = intent
        intent.putExtra("lat", tempNavigationData.getLat())
        intent.putExtra("long", tempNavigationData.getLong())
        intent.putExtra("floor", tempNavigationData.getFloor())
        startActivity(intent)
    }

    private fun beenToEveryLocation(): Boolean {
        return tour.getStops() == tour.getStopsVisited()
    }

    private fun findNextUnvisitedLocation() {
        val x: MutableList<Location> = tour.getTourStops()
        val y: MutableList<Boolean> = tour.getTourStopsVisited()

        for ((i, j) in x.withIndex()) {
            if (!y[i]) {
                nextLocation = x[i]
            }
        }
    }
}