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
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.activities.map.Marker
import com.hoffhaxx.app.concurs.misc.MapRepository
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.misc.data.UserLocation
import com.hoffhaxx.app.concurs.misc.web.WebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.HashMap
import kotlin.math.sqrt


class MapActivity : AppCompatActivity() {

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var userLatLng: LatLng

    private val filters = HashMap<String, Boolean>()

    lateinit var checkBoxTrash: CheckBox

    lateinit var buttonConfirm: Button
    lateinit var buttonCancel: Button
    lateinit var backgroundButtons: ImageView
    lateinit var textButtons: TextView
    
    private var isMarkerClicked: Boolean = false
    private val defaultMarker: Marker = Marker("", 0.0, 0.0, "", "", "")
    private var lastClickedMarker: Marker = defaultMarker

    private var action = ""

    private var clickableMarkers = true
    private var clickedMarker: Marker = defaultMarker

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        initFilters()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mLocationRequest = LocationRequest()

        userLatLng = if(SharedPreferencesRepository.userLocation != null) {
            LatLng(SharedPreferencesRepository.userLocation!!.latitude, SharedPreferencesRepository.userLocation!!.longitude)
        }else{
            LatLng(52.23, 21.01)
        }


        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        if (checkPermissionForLocation(this)) {
            startLocationUpdates()

            mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync { it ->
                //poczatek funkcji map
                googleMap = it
                googleMap.isMyLocationEnabled = true
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

                checkBoxTrash = findViewById(R.id.Trash)
                checkBoxTrash.isChecked = true
                checkBoxTrash.setOnCheckedChangeListener { buttonView, isChecked ->
                    setFilterValue("Trash", isChecked)
                }

                buttonConfirm = findViewById(R.id.Confirm)
                buttonCancel = findViewById(R.id.Cancel)
                backgroundButtons = findViewById(R.id.ButtonsBackground)
                textButtons = findViewById(R.id.ButtonsText)
                backgroundButtons.isVisible = false
                buttonConfirm.isVisible = false
                buttonCancel.isVisible = false
                textButtons.isVisible = false

                buttonConfirm.setOnClickListener {
                    if (action == "delete") {
                        removeMarker(lastClickedMarker)
                    } else if (action == "add") {
                        saveMarker(clickedMarker)
                        setFilterValue("Trash", true)
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

                googleMap.setOnMarkerClickListener { marker ->
                    if (clickableMarkers) {
                        if (marker.title == getString(R.string.trash)) {
                            if (lastClickedMarker.latitude != marker.position.latitude || lastClickedMarker.longitude != marker.position.longitude) {
                                marker.showInfoWindow()
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
                        "",
                        "",
                        ""
                    )

                    isMarkerClicked = false
                    lastClickedMarker = defaultMarker

                    val maxDistance = 100
                    val distance = distanceInMeters(
                        userLatLng.latitude,
                        userLatLng.longitude,
                        it.latitude,
                        it.longitude
                    )

                    if (clickableMarkers) {
                        if (distance < maxDistance) {
                            action = "add"
                            textButtons.text = getString(R.string.are_you_sure_you_want_to_add_a_trash)
                            buttonCancel.isVisible = true
                            buttonConfirm.isVisible = true
                            backgroundButtons.isVisible = true
                            textButtons.isVisible = true
                            clickableMarkers = false
                            clickedMarker = thisMarker
                        } else {
                            Toast.makeText(this, getString(R.string.far_away), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                googleMap.setOnInfoWindowClickListener { marker ->
                    if (marker.title == getString(R.string.trash)) {
                        val maxDistance = 100
                        val distance = distanceInMeters(
                            userLatLng.latitude,
                            userLatLng.longitude,
                            marker.position.latitude,
                            marker.position.longitude
                        )
                        if (distance < maxDistance) {
                            action = "delete"
                            textButtons.text = getString(R.string.are_you_sure_you_want_to_remove_a_trash)
                            buttonCancel.isVisible = true
                            buttonConfirm.isVisible = true
                            backgroundButtons.isVisible = true
                            textButtons.isVisible = true
                            clickableMarkers = false
                            setLastClickedMarker(marker.position.latitude, marker.position.longitude)
                        } else {
                            Toast.makeText(this, getString(R.string.far_away), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                refreshMap()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 19.0f))
            }//koniec funkcji map
        }else{
            finish()
        }

    }

    private fun setFilterValue(type: String, value: Boolean) {
        filters[type] = value
        if (type == "Trash") {
            checkBoxTrash.isChecked = value
        }
        refreshMap()
    }

    private fun initFilters()
    {
        filters["Trash"] = true
    }

    private fun setLastClickedMarker(lat: Double, lng: Double) = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Main) {
                val tempMarkers = MapRepository.getMarkers()
                if (tempMarkers != null) {
                    for(m: Marker in tempMarkers){
                        if(m.latitude == lat && m.longitude == lng){
                            lastClickedMarker = m
                        }
                    }
                }
            }
        } catch (e : WebClient.NetworkException) {
            withContext(Dispatchers.Main) {

            }
        }
    }

    private fun saveMarker(m : Marker) = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Main) {
                MapRepository.addMarkers(mutableListOf(m))
                refreshMap()
            }
        } catch (e : WebClient.NetworkException) {
            withContext(Dispatchers.Main) {

            }
        }
    }

    private fun removeMarker(m: Marker) = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Main){
                MapRepository.removeMarker(m)
                refreshMap()
            }
        } catch (e : WebClient.NetworkException) {
            withContext(Dispatchers.Main) {

            }
        }
    }

    private fun placeMarkerOnMap(m: Marker) {
        val location = LatLng(m.latitude, m.longitude)
        if(m.type == "Trash") {
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(getString(R.string.trash))
                    .snippet(m.addedDate)
                    .icon(BitmapDescriptorFactory.fromResource(R.raw.mapmarker32))
                    .visible(true)
            )
        }
    }

    private fun placeMarkersOfType(type: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Main) {
                val marOfType = MapRepository.getMarkers()?.filter {
                    it.type == type
                }
                if (marOfType != null) {
                    for (m in marOfType) {
                        placeMarkerOnMap(m)
                    }
                }
            }
        } catch (e : WebClient.NetworkException) {

        }
    }

    private fun refreshMap() = CoroutineScope(Dispatchers.IO).launch {
        withContext(Main){
            googleMap.clear()
            for((key, value) in filters) {
                if(value) {
                    placeMarkersOfType(key)
                }
            }
        }
    }

    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.gps_disabled)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }


    private fun startLocationUpdates() {

        // Create the location request to start receiving updates

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
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
        SharedPreferencesRepository.userLocation = UserLocation(userLatLng.latitude, userLatLng.longitude)
    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this@MapActivity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
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
