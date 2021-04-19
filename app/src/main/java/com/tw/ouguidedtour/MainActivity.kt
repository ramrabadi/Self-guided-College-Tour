package com.tw.ouguidedtour

import android.Manifest
import android.Manifest.permission
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import org.osmdroid.bonuspack.overlays.GroundOverlay
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import timber.log.Timber
import com.google.android.gms.location.*
import com.graphhopper.GHRequest
import com.graphhopper.GHResponse
import com.graphhopper.reader.osm.GraphHopperOSM
import com.graphhopper.routing.util.EncodingManager
import com.graphhopper.util.Parameters
import com.graphhopper.util.PointList
import com.tw.ouguidedtour.Data.NavigationData
import com.tw.ouguidedtour.Data.Tour
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.GraphHopperRoadManager
import org.osmdroid.views.MapController
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null
    private var mLocationPermissionApproved: Boolean = false
    private var mCameraPermissionApproved: Boolean = false
    private var mWifiEnabled: Boolean = false
    private var map: MapView? = null

    var endpoint: GeoPoint = GeoPoint(39.3262347, -82.1068644)
    var currentFloor = 1
    var endFloor = 1
    var displayFloor = 1
    val gpsRoadManager: RoadManager = GraphHopperRoadManager("b48048f0-1ee2-4459-ad43-9e5da2d005eb", false)
    var isLocationUpdating = true;
    var hopper1 = GraphHopperOSM()
    var hopper2 = GraphHopperOSM()//graph data and routing for each floor
    var hopper3 = GraphHopperOSM()
    val myGroundOverlay = GroundOverlay()
    var roadOverlay: Polyline = Polyline()
    var indoorOverlay: Polyline = Polyline()
    lateinit var destMarker: Marker
    lateinit var mLocationOverlay : MyLocationNewOverlay
    lateinit var locationMarker: Marker

    /** Location Listener Config */
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 5000
    private val FASTEST_INTERVAL: Long = 10000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest

    /** Variables for TourActivity */
    var tour: Tour = Tour()
    private var currentLocation: com.tw.ouguidedtour.Data.Location = com.tw.ouguidedtour.Data.Location()
    private var nextLocation: com.tw.ouguidedtour.Data.Location = com.tw.ouguidedtour.Data.Location()
    private var nextLocationId: String = "None"

    private var qrString = "None"

    private lateinit var mWifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("onCreate Called")

        //val scanQRCode: Button = findViewById(R.id.QRCodeButton2)
        //val scanQRCode: Button = findViewById(R.id.ScanQR)
        //used for temp json reader

        /** Init for bottomNavigationView */
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        /** Set bottomNavigationView */
        bottomNavigationView.selectedItemId = R.id.MapMenu
        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {startActivity(Intent(applicationContext, MainMenuActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.MapMenu -> {startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ScanQR -> {
                    val intentIntegrator = IntentIntegrator(this@MainActivity)
                    intentIntegrator.setBeepEnabled(false)
                    intentIntegrator.setCameraId(0)
                    intentIntegrator.setPrompt("SCAN")
                    intentIntegrator.setBarcodeImageEnabled(false)
                    intentIntegrator.initiateScan()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.FloorPlanMenu -> {


                    map!!.overlays.remove(destMarker)
                    map!!.invalidate()


                    destMarker = Marker(map)

                    if (displayFloor == 3) {
                        displayFloor = 1
                    }
                    else {
                        displayFloor += 1
                    }

                    if (endFloor != displayFloor) {
                        destMarker.position = GeoPoint(39.3260909, -82.1069895)
                    } else {
                        destMarker.position = endpoint
                    }
                    destMarker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    destMarker.title = "Destination"
                    if (displayFloor == currentFloor || displayFloor == endFloor) {
                        map!!.overlays.add(destMarker)
                    }
                    map!!.invalidate()


                    map!!.overlays.remove(mLocationOverlay)
                    map!!.overlays.remove(locationMarker)

                    if (currentFloor == displayFloor) {
                        map!!.overlays.add(mLocationOverlay)
                        if (!mLocationOverlay.isMyLocationEnabled) {
                            map!!.overlays.add(locationMarker)
                        }
                    }

                    map!!.invalidate()


                    if (displayFloor == 1) {
                        startLocationUpdates()
                        map!!.overlays.add(roadOverlay)//gps route added only for ground floor view
                        val d: Drawable? =
                            ResourcesCompat.getDrawable(resources, R.drawable.first_floor, null)
                        if (d != null) {
                            myGroundOverlay.image = d.mutate()
                        }
                    }
                    else if (displayFloor == 2) {
                        map!!.overlays.remove(roadOverlay)
                        stopLocationUpdates()
                        val d: Drawable? =
                            ResourcesCompat.getDrawable(resources, R.drawable.second_floor, null)
                        if (d != null) {
                            myGroundOverlay.image = d.mutate()
                        }
                    }
                    else if (displayFloor == 3) {
                        val d: Drawable? =
                            ResourcesCompat.getDrawable(resources, R.drawable.third_floor, null)
                        if (d != null) {
                            myGroundOverlay.image = d.mutate()
                        }
                    }

                    updateIndoor()

                    map!!.invalidate()
                    if (displayFloor == 1) {
                        map!!.controller.animateTo(mLocationOverlay.myLocation)
                    }
                    else {
                        map!!.controller.animateTo(GeoPoint(39.3261779, -82.106899))
                    }

                }
                R.id.VideoViewMenu -> {
                    if (currentLocation.getId() != "None") {
                        val mainIntent = Intent(this, TourActivity::class.java)
                        startActivity(mainIntent)
                    } else {
                        Toast.makeText(this, "Try visiting a location first.", Toast.LENGTH_SHORT).show()
                    }
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        });


        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        gpsRoadManager.addRequestOption("vehicle=foot")


        val policy: StrictMode.ThreadPolicy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //create the map


        map = findViewById<View>(R.id.map) as MapView

        val mapController = map!!.controller
        mapController.setZoom(18.0)
        mapController.setCenter(GeoPoint(39.3261779, -82.106899))

        map!!.setUseDataConnection(true)

        map!!.minZoomLevel = 12.0


        map!!.setTileSource(TileSourceFactory.MAPNIK)
        map!!.setMultiTouchControls(true)


        /** Draw image of Stocker on map */
        myGroundOverlay.position = GeoPoint(39.3261561, -82.1069852)
        val d: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.first_floor, null)
        if (d != null) {
            myGroundOverlay.image = d.mutate()
        }
        myGroundOverlay.setDimensions(130.0f)
        myGroundOverlay.transparency = 0.25f
        myGroundOverlay.bearing = -54.45F
        map!!.overlays.add(myGroundOverlay)

        destMarker =
            Marker(map)
        if (endFloor == currentFloor) {
            destMarker.position = endpoint
        }
        else {
            destMarker.position = GeoPoint(39.3260909, -82.1069895)
        }
        destMarker.setAnchor(
            Marker.ANCHOR_CENTER,
            Marker.ANCHOR_BOTTOM
        )
        destMarker.title = "Destination"
        map!!.overlays.add(destMarker)

        //enable location tracking
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map!!)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()

        locationMarker = Marker(map)



        // create graphhopper instances
        val osmFile1 = File(applicationContext.filesDir, "floor1.osm")
        val osmFile2 = File(applicationContext.filesDir,"floor2.osm")
        val osmFile3 = File(applicationContext.filesDir,"floor3.osm")
        val graphFolder1 = File(applicationContext.filesDir, "floor1")
        val graphFolder2 = File(applicationContext.filesDir, "floor2")
        val graphFolder3 = File(applicationContext.filesDir, "floor3")
        if (!graphFolder1.exists()) {
            graphFolder1.mkdir()
        }
        if (!graphFolder2.exists()) {
            graphFolder2.mkdir()
        }
        if (!graphFolder3.exists()) {
            graphFolder3.mkdir()
        }
        if (!osmFile1.exists()) {

            val bufferSize = 1024

            val filein: InputStream = this.resources.openRawResource(R.raw.floor1)
            val fileout = FileOutputStream(osmFile1)

            try {
                filein.copyTo(fileout, bufferSize)
            } finally {
                filein.close()
                fileout.flush()
                filein.close()
            }

        }
        if (!osmFile2.exists()) {

            val bufferSize = 1024

            val filein: InputStream = this.resources.openRawResource(R.raw.floor2)
            val fileout = FileOutputStream(osmFile2)

            try {
                filein.copyTo(fileout, bufferSize)
            } finally {
                filein.close()
                fileout.flush()
                filein.close()
            }

        }
        if (!osmFile3.exists()) {

            val bufferSize = 1024

            val filein: InputStream = this.resources.openRawResource(R.raw.floor3)
            val fileout = FileOutputStream(osmFile3)

            try {
                filein.copyTo(fileout, bufferSize)
            } finally {
                filein.close()
                fileout.flush()
                filein.close()
            }

        }

        hopper1.osmFile = osmFile1.absolutePath
        hopper1.setMinNetworkSize(1, 1)//graph is custom so import all nodes
        hopper1.encodingManager = EncodingManager("car, foot")
        hopper1.graphHopperLocation = graphFolder1.absolutePath
        hopper1.importOrLoad()

        hopper2.osmFile = osmFile2.absolutePath
        hopper2.setMinNetworkSize(1, 1)
        hopper2.encodingManager = EncodingManager("car, foot")
        hopper2.graphHopperLocation = graphFolder2.absolutePath
        hopper2.importOrLoad()

        hopper3.osmFile = osmFile3.absolutePath
        hopper3.setMinNetworkSize(1, 1)
        hopper3.encodingManager = EncodingManager("car, foot")
        hopper3.graphHopperLocation = graphFolder3.absolutePath
        hopper3.importOrLoad()



        @Suppress("SpellCheckingInspection") val waypoints = ArrayList<GeoPoint>()
        /*
        mLocationOverlay.runOnFirstFix {
            updateIndoor()
            runOnUiThread {

                mapController.animateTo(mLocationOverlay.myLocation)
                mapController.setZoom(18.0)
                val currentloc = mLocationOverlay.myLocation
                waypoints.add(currentloc)
                waypoints.add(GeoPoint(39.3259984,-82.1066439))//location of door
                val road: Road = gpsRoadManager.getRoad(waypoints)
                roadOverlay= RoadManager.buildRoadOverlay(road)
                map!!.overlays.add(roadOverlay)


            }
        }

         */

        map!!.overlays.add(mLocationOverlay)


        startLocationUpdates()
        map!!.invalidate()//refresh the map to apply changes

        //load osmdroid configuration
        val ctx = applicationContext
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    // Sets QR code value for use in TourActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent ?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val dataIntent = intent
                dataIntent.putExtra("id", result.contents)
                startActivity(dataIntent)
                qrString = result.contents
                val tourIntent = Intent(this, TourActivity::class.java)

                if (tour.getId() == "None") {
                    tour.setId(tour.get_tour_id(qrString,"Test.json", assets))
                    tour.load_list_of_stops(tour, qrString, "Test.json", assets)
                    currentLocation = tour.getLocation(tour, qrString)
                    nextLocation = tour.getLocation(tour, currentLocation.getNextLocationId().toString())
                    nextLocationId = nextLocation.getId()
                    val navdat = nextLocation.getNavData()
                    endFloor = navdat.getFloor()
                    endpoint = GeoPoint(navdat.getLat(),navdat.getLong())
                    tour.setToursStopVisited(tour, qrString)
                    tourIntent.putExtra("name", currentLocation.getName())
                    tourIntent.putExtra("videoUrl", currentLocation.getVideoUrl())
                    tourIntent.putExtra("description", currentLocation.getDescription())
                    tourIntent.putExtra("picture", currentLocation.getPicture())
                    startActivity(tourIntent)
                } else if (qrString == nextLocationId) {
                    //Toast.makeText(this, "nextLocation", Toast.LENGTH_SHORT).show()
                    tour.setToursStopVisited(tour, qrString)
                    if ((tour.getStops() - tour.getStopsVisited()) == 1) {
                        currentLocation = nextLocation
                        Toast.makeText(this, "You are headed to the Last Location!", Toast.LENGTH_LONG).show()
                        tourIntent.putExtra("name", currentLocation.getName())
                        tourIntent.putExtra("videoUrl", currentLocation.getVideoUrl())
                        tourIntent.putExtra("description", currentLocation.getDescription())
                        tourIntent.putExtra("picture", currentLocation.getPicture())
                        startActivity(tourIntent)
                    }
                    currentLocation = nextLocation

                    nextLocationId = nextLocation.getNextLocationId()
                    nextLocation = tour.getLocation(tour, nextLocationId)
                    val navdat = nextLocation.getNavData()
                    endFloor = navdat.getFloor()
                    endpoint = GeoPoint(navdat.getLat(),navdat.getLong())

                    tourIntent.putExtra("name", currentLocation.getName())
                    tourIntent.putExtra("videoUrl", currentLocation.getVideoUrl())
                    tourIntent.putExtra("description", currentLocation.getDescription())
                    tourIntent.putExtra("picture", currentLocation.getPicture())
                    startActivity(tourIntent)
                } else {
                    if (!tour.haveBeenToLocation(tour, qrString) && (tour.getStops() - tour.getStopsVisited()) == 2) {
                        tour.setToursStopVisited(tour, qrString)
                        currentLocation = tour.getLocation(tour, qrString)

                        nextLocationId = tour.findNextUnvisitedLocation(tour)
                        val navdat = nextLocation.getNavData()
                        endFloor = navdat.getFloor()
                        endpoint = GeoPoint(navdat.getLat(),navdat.getLong())

                        //Toast.makeText(this, "findNextUnvisited", Toast.LENGTH_SHORT).show()

                        nextLocation = tour.getLocation(tour, nextLocationId)
                        tourIntent.putExtra("name", currentLocation.getName())
                        tourIntent.putExtra("videoUrl", currentLocation.getVideoUrl())
                        tourIntent.putExtra("description", currentLocation.getDescription())
                        tourIntent.putExtra("picture", currentLocation.getPicture())
                        startActivity(tourIntent)
                    }

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /** Lifecycle Methods **/

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")

        mLocationPermissionApproved =
            (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)

        mCameraPermissionApproved =
            (ActivityCompat.checkSelfPermission(this, permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)

        /** Checks to see if wifi is currently enabled and if wifi is not enabled then
         *   ask the user to enable wifi.  .setWifiEnabled() was deprecated in API 29**/
        mWifiEnabled = mWifiManager.isWifiEnabled
        Timber.i("mWifiEnabled: $mWifiEnabled")

        if (!mWifiEnabled) {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setMessage("You do not have Wifi Enabled right now. Please enable wifi so that the app can help you navigate around Stocker.")
                setTitle("Wifi Required")
                setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> }
            }
            val dialog = builder.create()
            dialog.show()
        }
        /*moved updating of loc variables to qr return in attempting to debug update location
        Toast.makeText(this, nextLocation.getId(), Toast.LENGTH_SHORT).show()
        if (nextLocation.getId() != "None" ) {
            val tempNavData = nextLocation.getNavData()
            updateDestination(
                tempNavData.getLat(), tempNavData.getLong(), tempNavData.getFloor(), map!!,
                map!!.controller, mLocationOverlay, currentFloor
            )
        }

         */


        // Resume map updating of ui
        map!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")

        // Pause updating of map ui
        map!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")

        // Stop getting GPS location
        stopLocationUpdates()
    }
    
    private fun updateDestination(
        //data for next stop
        lat: Double,
        lon: Double,
        floor: Int,

        //the rest of this is arguments so that the map can be updated
        map : MapView,
        mapController: IMapController,
        mLocationOverlay : MyLocationNewOverlay,
        current_floor: Int
        ) {


        @Suppress("SpellCheckingInspection") val waypoints = ArrayList<GeoPoint>()

        map.overlays.remove(destMarker)
        map.overlays.remove(roadOverlay)
        map.invalidate()

        currentFloor = endFloor
        locationMarker.position = endpoint

        endpoint = GeoPoint(lat,lon)
        endFloor = floor

        destMarker = Marker(map)

        if (floor != current_floor) {
            destMarker.position = GeoPoint(39.3260909, -82.1069895)
        } else {
            destMarker.position = endpoint
        }
        destMarker.setAnchor(
            Marker.ANCHOR_CENTER,
            Marker.ANCHOR_BOTTOM
        )
        destMarker.title = "Destination"
        map.overlays.add(destMarker)
        map.invalidate()
        updateIndoor()

        if (isLocationUpdating) {

            runOnUiThread {
                mapController.animateTo(mLocationOverlay.myLocation)
                mapController.setZoom(18.0)
                val currentloc = mLocationOverlay.myLocation
                waypoints.add(currentloc)
                waypoints.add(GeoPoint(39.3259984,-82.1066439))//door
                val road: Road = gpsRoadManager.getRoad(waypoints)
                roadOverlay = RoadManager.buildRoadOverlay(road)
                map.overlays.add(roadOverlay)

                map.invalidate()
            }
        }

    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            //check for update on current location

            locationResult.lastLocation

            onLocationChanged(locationResult.lastLocation)
        }

        fun onLocationChanged(location: Location) {
            mLastLocation = location

            // Need to keep can return null.
            @Suppress("SENSELESS_COMPARISON")
            if ( mLastLocation != null) {

                //check for switch to/from indoor navigation
                if (mLocationOverlay.myLocation != null) {
                    gpsEnableCheck()
                    updateIndoor()

                    //update and replace route overlay
                    //note overlay location is used and not actual location
                    // is disabled, i.e. user is indoors this is still required to update the route
                    map!!.overlays.remove(roadOverlay)
                    map!!.invalidate()

                    if (isLocationUpdating) {//only redraw roadOverlay if gps routing is enabled
                        runOnUiThread {

                            val waypoints = ArrayList<GeoPoint>()
                            waypoints.add(mLocationOverlay.myLocation)
                            waypoints.add(GeoPoint(39.3259984,-82.1066439))//door
                            roadOverlay =
                                RoadManager.buildRoadOverlay(gpsRoadManager.getRoad(waypoints))

                            map!!.overlays.add(roadOverlay)
                            map!!.invalidate()
                        }

                    }
                }

            }

        }
    }


    /**
     * Function: startLocationUpdates
     *
     * Purpose: Starts sending location requests and is ready to recieve them
     *
     * Pre-condition: Has permissions for "Access Fine Location", and if not returns nothing
     *
     * Post-condition: starts a callback to receive location updates
     */
    private fun startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
            Looper.myLooper())
    }

    /**
     * Function: stopLocationUpdates
     *
     * Purpose: Stops receiving location updates
     *
     * Post-condition: Stops sending / receiving location information.
     */
    private fun stopLocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    /** Tour Functions */
    // TODO use tempNavigationData.getLat() for lat
    fun setNewPath() {
        val tempNavigationData: NavigationData = nextLocation.getNavData()

        //updateDestination(tempNavigationData.getLat())
    }



    /**
     * Checks if location is within Stocker
     * disable following gps location on map to prevent inaccuracy from disrupting navigation
     * Currently set for 30 m from center of building
     * re-enables if outside that radius
     */
    private fun gpsEnableCheck() {
        val enableLoc = GeoPoint(39.3261291,-82.1069648)//coordinates for center of stocker


        if ( enableLoc.distanceToAsDouble(mLocationOverlay.myLocation) < 100.0 ) {
            if (isLocationUpdating == true) {
                locationMarker.position = mLocationOverlay.myLocation
                map!!.overlays.remove(mLocationOverlay)
                map!!.overlays.add(locationMarker)
                map!!.invalidate()
                isLocationUpdating = false
            }

        }
        else {
            if (isLocationUpdating == false) {
                map!!.overlays.remove(locationMarker)
                map!!.overlays.add(mLocationOverlay)
                map!!.invalidate()
                isLocationUpdating = true
            }
        }
        updateIndoor()


    }

    fun updateIndoor() {
        map!!.overlays.remove(indoorOverlay)
        map!!.invalidate()

        var startPoint: GeoPoint = GeoPoint(39.3260909, -82.1069895)
        var routePoint: GeoPoint = GeoPoint(39.3260909, -82.1069895)
        val elevator: GeoPoint = GeoPoint(39.3260909, -82.1069895)
        val door: GeoPoint = GeoPoint(39.3259984,-82.1066439)

        if (displayFloor == currentFloor) {
            if (displayFloor == 1 ) {
                if (isLocationUpdating) {
                    startPoint = door
                }
                else {
                    startPoint = locationMarker.position
                }
            }
            else {
                startPoint = locationMarker.position
            }

            if (currentFloor == endFloor) {
                routePoint = endpoint
            }
            else {
                routePoint = elevator
            }
        }
        else {
            startPoint = elevator
            if (displayFloor == endFloor) {
                routePoint = endpoint
            }
        }



        if (displayFloor == currentFloor || displayFloor == endFloor ) {
            val req = GHRequest(
                startPoint.latitude,startPoint.longitude, //start
                routePoint.latitude,routePoint.longitude       //end
            ).setAlgorithm(Parameters.Algorithms.DIJKSTRA_BI) //setWeighting("fastest");
            lateinit var rsp: GHResponse
            when (displayFloor) {
                1 -> {
                    rsp = hopper1.route(req)
                }
                2 -> {
                    rsp = hopper2.route(req)
                }
                3 -> {
                    rsp = hopper3.route(req)
                }
            }


            runOnUiThread {
                if (!rsp.hasErrors()) {
                    indoorOverlay = Polyline()
                    val tmp: PointList = rsp.best.points
                    for (i in 0 until rsp.best.points.size) {
                        indoorOverlay.addPoint(GeoPoint(tmp.getLatitude(i), tmp.getLongitude(i)))
                    }

                    indoorOverlay.color = Color.BLUE
                    indoorOverlay.width = 5f

                    map!!.overlays.add(indoorOverlay)
                    map!!.invalidate()

                }
            }
        }
    }

}

