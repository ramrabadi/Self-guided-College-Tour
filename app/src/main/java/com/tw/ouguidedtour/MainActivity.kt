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
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
import com.google.zxing.integration.android.IntentIntegrator
//used for temp json reader
import com.google.gson.reflect.TypeToken
import org.osmdroid.bonuspack.routing.GraphHopperRoadManager
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.views.MapController
import android.location.LocationListener as LocationListener


//used for temp json reader
data class Stop(val id: String, val name: String, val next_stop: Int, val url: String, val desc: String, val tour_id: String) {
}
data class Tour(val id: String, val name: String, val stops: Int, val locations: List<Stop>) {
}

class MainActivity : AppCompatActivity() {

    private var locationManager : LocationManager? = null
    private val ACCESS_FINE_LOCATION_RQ = 101
    private val ACCESS_CAMERA_RQ = 102
    private var mLocationPermissionApproved: Boolean = false
    private var mCameraPermissionApproved: Boolean = false
    private var mWifiEnabled: Boolean = false
    private var map: MapView? = null

    var endpoint : GeoPoint = GeoPoint(39.3260082, -82.1066659)
    var current_floor = 1
    val gpsroadManager: RoadManager = GraphHopperRoadManager("b48048f0-1ee2-4459-ad43-9e5da2d005eb", false)
    var roadOverlay: Polyline = Polyline()
    lateinit var destMarker: Marker
    lateinit var mLocationOverlay : MyLocationNewOverlay

    //location listener config
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 5000
    private val FASTEST_INTERVAL: Long = 10000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest

    private var tour = ArrayList<Tour>()
    private var qr_string = ""







    /** These are for the Checking if Wifi RTT is compatible with the phone **/
    //private var deviceCompatible: Boolean = false
    //private lateinit var mWifiRttManager: WifiRttManager

    private lateinit var mWifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("onCreate Called")

        //used for temp json reader
        /*
        //this is not formatted for current json file setup
        var s: String =
            applicationContext.assets.open("Test.json").bufferedReader().use { it.readText() }
        val Tourlisttype = object : TypeToken<List<Tour>>() {}.type
        val gson = GsonBuilder().create()
        tour = gson.fromJson<ArrayList<Tour>>(s, Tourlisttype)
        */

        mWifiManager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Check if user has Android API 28 or higher
        if (Build.VERSION.SDK_INT >= 28) {
            // Initialize Camera button


            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            gpsroadManager.addRequestOption("vehicle=foot")


            // Init of Video button temp
            val button = findViewById<Button>(R.id.VideoButton)
            button.setOnClickListener {
                val intent = Intent(this, Activity2::class.java)
                startActivity(intent)
            }

            // Display Floor Plan Button
            val floorPlanButton: Button = findViewById(R.id.FloorPlanButton)
            floorPlanButton.setOnClickListener {
                // Display Floor Plan
                val intent = Intent(this, FloorPlan::class.java)
                startActivity(intent)
            }

            val policy: StrictMode.ThreadPolicy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Configuration.getInstance().isDebugMode = true
            Configuration.getInstance().isDebugTileProviders = true
            //create the map
            val mainTileProvider = MapTileProviderBasic(applicationContext)
            mainTileProvider.tileSource = TileSourceFactory.MAPNIK
            val mainTileOverlay = TilesOverlay(mainTileProvider, this.baseContext)
            mainTileOverlay.loadingBackgroundColor = (Color.TRANSPARENT)

            map = findViewById<View>(R.id.map) as MapView

            val mapController = map!!.controller
            mapController.setZoom(18.0)
            mapController.setCenter(GeoPoint(39.3261779, -82.106899))

            map!!.setUseDataConnection(true)
            map!!.overlays.add(mainTileOverlay)
            map!!.minZoomLevel = 12.0

            //map!!.setTileSource(TileSourceFactory.MAPNIK)
            map!!.setMultiTouchControls(true)

            /* Tile provider for interior map This will draw indoor map, but is not currently compatible with routefinding
            
            val indoorTileprovider = MapTileProviderBasic(applicationContext)
            val indoorTileSource = XYTileSource(
                "first_floor",
                15,
                19,
                256,
                ".png",
                arrayOf("")
            )
            
             

            indoorTileprovider.tileSource = indoorTileSource
            val indoorTileOverlay = TilesOverlay(indoorTileprovider, this.baseContext)
            indoorTileOverlay.loadingBackgroundColor = Color.TRANSPARENT
            map!!.overlays.add(indoorTileOverlay)


             */



            val dest_floor = 3
            val dest_loc = GeoPoint(39.3261779, -82.106899)


            if (dest_floor != current_floor) {
                endpoint = GeoPoint(39.3260909, -82.1069895)
            } else {
                endpoint = dest_loc
            }


            //draw image of stocker interior on map
            val myGroundOverlay = GroundOverlay()
            myGroundOverlay.setPosition(GeoPoint(39.3261511, -82.1069252))
            val d: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.first_floor, null)
            if (d != null) {
                myGroundOverlay.setImage(d.mutate())
            }
            myGroundOverlay.setDimensions(130.0f)
            myGroundOverlay.setTransparency(0.25f)
            myGroundOverlay.setBearing(-54.5F)
            map!!.overlays.add(myGroundOverlay)




            destMarker =
                Marker(map)
            destMarker.position = endpoint
            destMarker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )
            destMarker.setTitle("Destination")
            map!!.overlays.add(destMarker)

            //enable location tracking
            mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map!!)
            mLocationOverlay.enableMyLocation()
            mLocationOverlay.enableFollowLocation()

            //enable navigation

            val waypoints = ArrayList<GeoPoint>()

            mLocationOverlay.runOnFirstFix {
                runOnUiThread {

                    mapController.animateTo(mLocationOverlay.myLocation)
                    mapController.setZoom(18.0)
                    val currentloc = mLocationOverlay.myLocation
                    waypoints.add(currentloc)
                    waypoints.add(endpoint)
                    val road: Road = gpsroadManager.getRoad(waypoints)
                    roadOverlay= RoadManager.buildRoadOverlay(road)
                    map!!.overlays.add(roadOverlay)

                }
            }

            map!!.overlays.add(mLocationOverlay)

            startLocationUpdates()
            map!!.invalidate()//refresh the map to apply changes

            //load osmdroid configuration
            val ctx = applicationContext
            Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))


            //isDeviceCompatible()


            // If Android API is below 28, display floor plan
        } else {
            val below28API = Intent(this, FloorPlan::class.java)
            startActivity(below28API)
        }
        val mainMenuButton: Button = findViewById(R.id.MainMenuButton)
        mainMenuButton.setOnClickListener {
            val dataIntent = Intent(this, MainMenuActivity::class.java)
            startActivity(dataIntent)

        }

        checkForPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            "Fine Location",
            ACCESS_FINE_LOCATION_RQ
        )
        checkForPermissions(
            android.Manifest.permission.CAMERA,
            "Camera",
            ACCESS_CAMERA_RQ
        )



        val scanQRCode: Button = findViewById(R.id.QRCodeButton2)
        scanQRCode.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@MainActivity)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan()
            var i = 0
            while (i < tour.size && tour[0].locations[i].name == qr_string) {
                i += 1;
            }


        }

    }

    // Sends QR data to Database activity
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent ?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                qr_string = result.contents
                /*
                val dataIntent = Intent(this, DataActivity::class.java)
                dataIntent.putExtra("QRData", result.contents)
                startActivity(dataIntent)
                 */
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    /** Lifecycle Methods **/

    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")

        // Start getting GPS location if outside

        // Start getting Wifi RTT ranging if inside
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
                setMessage("You do not have Wifi Enabled right now")
                setTitle("Wifi Required")
                setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> }
            }
            val dialog = builder.create()
            dialog.show()
        }


        // Resume playing video
        map!!.onResume()//resume map updating of ui
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")

        // Pause video
        map!!.onPause()//pause updating of map ui
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")

        // Stop getting GPS location
        stoplocationUpdates()

        // Stop getting Wifi RTT ranging
    }
    
    private fun updateDestination(
        //data for next stop
        lat: Double,
        lon: Double ,
        floor: Integer,

        //the rest of this is arguments so that the map can be updated
        map : MapView,
        mapController: MapController,
        mLocationOverlay : MyLocationNewOverlay,
        current_floor: Integer
        ) {


        val waypoints = ArrayList<GeoPoint>()

        map.overlays.remove(destMarker)
        map.overlays.remove(roadOverlay)
        map.invalidate()

        if (floor != current_floor) {
            endpoint = GeoPoint(39.3260909, -82.1069895)
        } else {
            endpoint = GeoPoint(lat, lon)
        }


        destMarker =
            Marker(map)
        destMarker.position = endpoint
        destMarker.setAnchor(
            Marker.ANCHOR_CENTER,
            Marker.ANCHOR_BOTTOM
        )
        destMarker.setTitle("Destination")
        map.overlays.add(destMarker)







            runOnUiThread {
                mapController.animateTo(mLocationOverlay.myLocation)
                mapController.setZoom(18.0)
                val currentloc = mLocationOverlay.myLocation
                waypoints.add(currentloc)
                waypoints.add(endpoint)
                val road: Road = gpsroadManager.getRoad(waypoints)
                roadOverlay = RoadManager.buildRoadOverlay(road)
                map.overlays.add(roadOverlay)

                map.invalidate()
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
            if (mLastLocation != null) {

                //check for switch to/from indoor navigation
                if (mLocationOverlay.myLocation != null) {
                    gpsEnableCheck()


                    //update and replace route overlay
                    //note overlay location is used and not actual location
                    //if followlocation is disabled, i.e. user is indoors this is still required to update the route
                    map!!.overlays.remove(roadOverlay)
                    map!!.invalidate()
                    runOnUiThread {

                        val waypoints = ArrayList<GeoPoint>()
                        waypoints.add(mLocationOverlay.myLocation)
                        waypoints.add(endpoint)
                        roadOverlay =
                            RoadManager.buildRoadOverlay(gpsroadManager.getRoad(waypoints))

                        map!!.overlays.add(roadOverlay)
                        map!!.invalidate()
                    }
                }
            }

        }

    }

    protected fun startLocationUpdates() {

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

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    /** Functions which check for permission and request permissions **/

    /**
     *  Function: checkForPermissions
     *
     *  Purpose: This checks if the given permission has been granted or not, if it has been granted it displays a Toast to the screen
     *           sawing that the permission has been granted if not then it calls the requestPermissions function from ActivityCompat,
     *           this is the function that will be called to check and if not already granted calls other functions to request the permissions.
     *
     *  Parameters:     permission: String, Exp(android.Manifest.permission.ACCESS_FINE_LOCATION) This is a permission which must be in the Manifest.xml file.
     *
     *                  name: String, This is a string value that is a name given to the Permission by the coder and will be used if the permission has not
     *                                  been given already.
     *
     *                  requestCode: Int, This is a integer value that is created by the coder it could be any value only used later to check which permission needs
     *                                     to be used to request the permission
     *
     *  Pre-condition:  Valid input
     *
     *  Post-condition: If the permission being checked is granted sends a toast to the screen. If not calls showWhyRequest which displays a prompt for why we are asking
     *                  for this permission to be granted.
     *
     */
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        when {
            ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale(permission) -> showWhyRequest(
                permission,
                name,
                requestCode
            )

            else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }


    /**
     *  Function: showWhyRequest
     *
     *  Purpose: Shows a dialog box in the middle of the screen telling the user why we are going to request location permissions
     *
     *  Parameters: permission: String, Exp(android.Manifest.permission.ACCESS_FINE_LOCATION) This is a permission which must be in the Manifest.xml file.
     *
     *              name: String, This is a string value that is a name given to the Permission by the coder and will be used if the permission has not
     *                            been given already.
     *
     *             requestCode: Int, This is a integer value that is created by the coder it could be any value only used later to check which permission needs
     *                               to be used to request the permission
     *
     *  Pre-condition: Valid input
     *
     *  Post-condition: Displays dialog box and then once the user hit the OK button then stack goes back to checkForPermissions and to the else statement
     *
     */
    private fun showWhyRequest(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required help navigate you to the Stocker and through Stocker.")
            setTitle("Permission Required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }

        val dialog = builder.create()

        dialog.show()
    }

    /**
     *  Function: override of onRequestPermissionResult
     *
     *  Purpose: Shows a dialog box in the middle of the screen telling the user why we are going to request location permissions
     *
     *  Parameters: context: this@MainActivity
     *
     *              permission: String, Exp(android.Manifest.permission.ACCESS_FINE_LOCATION) This is a permission which must be in the Manifest.xml file
     *
     *             grandResults: IntArray,
     *
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission denied", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }

        when (requestCode) {
            ACCESS_FINE_LOCATION_RQ -> innerCheck("Fine Location")
            ACCESS_CAMERA_RQ -> innerCheck("Camera")
        }
    }

    /**Checks if Wifi Rtt is supported
     *
     *  This isn't required for use, but is needed for error handling.
     *
     * **/

    private fun isDeviceCompatible(){
        /** Error this always returns false **/
        val isCompatible = this.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)

        Timber.i("isCompatible: $isCompatible")
//        if(isCompatible){
//            Timber.i("Device is compatible")
//            Toast.makeText(applicationContext, "Wifi Rtt is Supported", Toast.LENGTH_SHORT).show()
//            //deviceCompatible = true
//
//        }else{
//            val wifiRttManager: WifiRttManager =getSystemService(Context.WIFI_RTT_RANGING_SERVICE) as WifiRttManager
//            val myReceiver = object : BroadcastReceiver(){
//                override fun onReceive(context: Context, intent: Intent){
//                    if(wifiRttManager.isAvailable){
//                        Timber.i("Device is compatible")
//                        Toast.makeText(
//                            applicationContext,
//                            "Wifi Rtt is Supported",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }else{
//                        Timber.i("Device is not compatible")
//                        Toast.makeText(
//                            applicationContext,
//                            "Wifi Rtt is not Supported",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//            registerReceiver(
//                myReceiver,
//                IntentFilter(WifiRttManager.ACTION_WIFI_RTT_STATE_CHANGED)
//            )
//        }
    }

    /**Checks if location is within stocker
     * disable following gps location on map to prevent innacuracy from disrupting navigation
     * Currently set for 30 m from center of building
     * reenables if outside that radius
     */
    private fun gpsEnableCheck() {
        val enableLoc = GeoPoint(39.3261291,-82.1069648)//coordinates for center of stocker


        if ( enableLoc.distanceToAsDouble(mLocationOverlay.myLocation) < 30.0 ) {
            mLocationOverlay.disableFollowLocation()
        }
        else {
            mLocationOverlay.enableFollowLocation()
        }


    }

}

