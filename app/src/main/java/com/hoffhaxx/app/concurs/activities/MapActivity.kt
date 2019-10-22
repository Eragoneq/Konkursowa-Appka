package com.hoffhaxx.app.concurs.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.activities.map.Marker
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.misc.fromJson
import kotlin.math.sqrt


class MapActivity : AppCompatActivity() {

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    private var userLatLng = LatLng(52.23, 21.01)

    private val filters = HashMap<String, Boolean>()

    private fun initFilters()
    {
        filters["Trash"] = true
        filters["Malysz"] = false
    }
    
    private var isMarkerClicked: Boolean = false
    private val defaultMarker: Marker = Marker("", 0.0, 0.0, false, "")
    private var lastClickedMarker: Marker = defaultMarker

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        initFilters()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mLocationRequest = LocationRequest()

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }


        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { it ->
            //poczatek funkcji map
            googleMap = it
            googleMap.isMyLocationEnabled = true
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

            val checkBoxTrash = findViewById<CheckBox>(R.id.Trash)
            checkBoxTrash.isChecked = true
            checkBoxTrash?.setOnCheckedChangeListener { buttonView, isChecked ->
                filters["Trash"] = isChecked
                refreshMap()
            }
            val checkBoxMalysz = findViewById<CheckBox>(R.id.Malysz)
            checkBoxMalysz.isChecked = false
            checkBoxMalysz?.setOnCheckedChangeListener { buttonView, isChecked ->
                filters["Malysz"] = isChecked
                refreshMap()
            }

            fun setFilterValue(type: String, value: Boolean) {
                filters[type] = value
                if(type == "Trash") {
                    checkBoxTrash.isChecked = value
                }else if(type == "Malysz"){
                    checkBoxMalysz.isChecked = value
                }
            }

            var action = ""

            var clickableMarkers = true
            var clickedMarker: Marker = defaultMarker

            val buttonConfirm = findViewById<Button>(R.id.Confirm)
            val buttonCancel = findViewById<Button>(R.id.Cancel)
            val backgroundButtons = findViewById<ImageView>(R.id.ButtonsBackground)
            val textButtons = findViewById<TextView>(R.id.ButtonsText)
            backgroundButtons.isVisible = false
            buttonConfirm.isVisible = false
            buttonCancel.isVisible = false
            textButtons.isVisible = false

            buttonConfirm.setOnClickListener {
                if(action == "delete") {
                    removeMarker(clickedMarker)
                    refreshMap()
                } else if(action == "add") {
                    saveMarker(clickedMarker)
                    setFilterValue("Trash", true)
                    refreshMap()
                }
                buttonCancel.isVisible = false
                buttonConfirm.isVisible = false
                backgroundButtons.isVisible = false
                textButtons.isVisible = false
                clickableMarkers = true

            }

            buttonCancel.setOnClickListener {
                buttonCancel.isVisible = false
                buttonConfirm.isVisible = false
                backgroundButtons.isVisible = false
                textButtons.isVisible = false
                clickableMarkers = true
            }

            //googleMap.uiSettings.setZoomControlsEnabled(true)

            googleMap.setOnMarkerClickListener { marker ->
                val thisMarker = Marker(
                    marker.title,
                    marker.position.latitude,
                    marker.position.longitude,
                    false,
                    "user name"
                )
                Toast.makeText(this, thisMarker.type, Toast.LENGTH_SHORT).show()
                if(clickableMarkers) {
                    if(marker.title == "Trash") {
                        if(!isMarkerClicked || lastClickedMarker != thisMarker) {
                            marker.showInfoWindow()
                            lastClickedMarker = thisMarker
                            isMarkerClicked = true
                        } else {
                            isMarkerClicked = false
                            lastClickedMarker = defaultMarker
                        }
                    } else {
                        marker.showInfoWindow()
                    }
                }
                true
            }

            googleMap.setOnMapClickListener {
                isMarkerClicked = false
                lastClickedMarker = defaultMarker
            }

            googleMap.setOnMapLongClickListener {
                val thisMarker = Marker(
                    "Trash",
                    it.latitude,
                    it.longitude,
                    false,
                    "user name"
                )

                isMarkerClicked = false
                lastClickedMarker = defaultMarker

                val maxDistance = 100
                val distance = distanceInMeters(userLatLng.latitude, userLatLng.longitude, it.latitude, it.longitude)

                if(clickableMarkers) {
                    if(distance < maxDistance) {
                        action = "add"
                        textButtons.text = "Are you sure you want to add a Trash?"
                        buttonCancel.isVisible = true
                        buttonConfirm.isVisible = true
                        backgroundButtons.isVisible = true
                        textButtons.isVisible = true
                        clickableMarkers = false
                        clickedMarker = thisMarker
                    } else {
                        Toast.makeText(this, "You are too far away", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            /*googleMap.setOnMapLongClickListener {
                val thisMarker = Marker(
                    "Malysz",
                    it.latitude,
                    it.longitude,
                    false,
                    "user name"
                )
                if(clickableMarkers) {
                    saveMarker(thisMarker)
                    setFilterValue("Malysz", true)
                    refreshMap()
                }
            }*/

            googleMap.setOnInfoWindowClickListener {marker ->
                val thisMarker = Marker(
                    marker.title,
                    marker.position.latitude,
                    marker.position.longitude,
                    false,
                    "user name"
                )
                if(marker.title == "Trash") {
                    action = "delete"
                    textButtons.text = "Are you sure you want to remove a Trash?"
                    buttonCancel.isVisible = true
                    buttonConfirm.isVisible = true
                    backgroundButtons.isVisible = true
                    textButtons.isVisible = true
                    clickableMarkers = false
                    clickedMarker = thisMarker
                }
            }

            refreshMap()
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 19.0f))
        }//koniec funkcji map

    }

    private fun getMarkers(): MutableList<Marker> {
        val markersJson = SharedPreferencesRepository.marker

        return if (markersJson != null)
            Gson().fromJson(markersJson)
        else
            mutableListOf()
    }

    private fun saveMarker(m : Marker) {
        val markersTemp = getMarkers()
        markersTemp.add(m)
        SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
    }

    private fun removeMarker(m: Marker) {
        val markersTemp = getMarkers()
        markersTemp.remove(m)
        SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
    }

    private fun placeMarkerOnMap(m: Marker) {
        val location = LatLng(m.latitude, m.longitude)
        if(m.type == "Trash") {
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(m.type)
                    .snippet(m.user)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                    .visible(true)
            )
        } else if(m.type == "Malysz") {
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(m.type)
                    .snippet(m.user)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.malysz))
                    .visible(true)
            )
        }
    }

    private fun placeMarkersOfType(type: String) {
        val marOfType = getMarkers().filter {
            it.type == type
        }
        for (m in marOfType) {
            placeMarkerOnMap(m)
        }
    }

    private fun refreshMap() {
        googleMap.clear()
        for((key, value) in filters) {
            if(value) {
                placeMarkersOfType(key)
            }
        }
    }

    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                //finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()


    }


    private fun startLocationUpdates() {

        // Create the location request to start receiving updates

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

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined

        mLastLocation = location
        userLatLng = locationToLatLng(mLastLocation)
    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this@MapActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    private fun distanceInMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val R = 6371000
        val dLat = degreesToRadians(lat2 - lat1)
        val dLng = degreesToRadians(lng2 - lng1)
        val a = kotlin.math.sin(dLat/2) * kotlin.math.sin(dLat/2) +
                kotlin.math.cos(degreesToRadians(lat1)/2) * kotlin.math.cos(degreesToRadians(lat2)/2) *
                kotlin.math.sin(dLng/2) * kotlin.math.sin(dLng/2)
        val c = 2 * kotlin.math.atan2(sqrt(a), sqrt(1-a))

        return R * c
    }

    private fun degreesToRadians(deg: Double) : Double
    {
        return deg / 180 * kotlin.math.PI
    }

    private fun locationToLatLng(location: Location) : LatLng
    {
        return LatLng(location.latitude, location.longitude)
    }

}
