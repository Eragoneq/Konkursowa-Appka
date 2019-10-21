package com.hoffhaxx.app.concurs.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.hoffhaxx.app.concurs.activities.map.Marker
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.misc.fromJson
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.RuntimeExecutionException

const val PERMISSION_REQUEST = 10

class MapActivity : AppCompatActivity() {

    /*lateinit var locationManager: LocationManager
    private var hasGps = false
    //private var hasNetwork = false
    private var locationGps : Location? = null
    //private var locationNetwork : Location? = null

    @SuppressLint("MissingPermission")
    private fun getLocation()
    {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        //hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(hasGps/* || hasNetwork*/) {

            if(hasGps)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0F, object : LocationListener{
                    override fun onLocationChanged(location: Location?) {
                        if(location!=null)
                        {
                            locationGps = location
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderEnabled(provider: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderDisabled(provider: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGpsLocation!=null)
                {
                    locationGps = localGpsLocation
                }
            }*/
            /*if(hasNetwork)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener{
                    override fun onLocationChanged(location: Location?) {
                        if(location!=null)
                        {
                            locationNetwork = location
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderEnabled(provider: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderDisabled(provider: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if(localNetworkLocation!=null)
                {
                    locationNetwork = localNetworkLocation
                }
            }

            if(locationGps!=null/* && locationNetwork!=*/)
            {
                //if(locationGps!!.accuracy > locationNetwork!!.accuracy) {
                    //userLatLng = LatLng(locationNetwork!!.latitude, locationNetwork!!.longitude)
                //}else{
                    //userLatLng = LatLng(locationGps!!.latitude, locationGps!!.longitude)
                    Toast.makeText(this, "robie elo", Toast.LENGTH_LONG).show()
                //}
            }

        }else{

        }
    }*/

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    fun checkPermission(context: Context, permissionArray: Array<String>) : Boolean
    {
        var allSuccess = true
        for(i in permissionArray.indices)
        {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
            {
                allSuccess = false
            }
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST)
        {
            var allSuccess = true
            for(i in permissions.indices)
            {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    allSuccess = false
                    var requestAgain = shouldShowRequestPermissionRationale(permissions[i]) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    if(requestAgain)
                    {

                    }else{

                    }
                }
            }
        }
    }

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

    //var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        var userLatLng : LatLng = LatLng(0.0, 0.0)
        val context: Context = this
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!checkPermission(context, permissions))
            {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        /*fun getLocation()
        {
            if(checkPermission(context, permissions))
            {
                fusedLocationClient?.lastLocation?.addOnSuccessListener(this
                ) { location : Location? ->
                    if(location == null) {
                        //nakaz włączenia lokalizacji/cofniecie
                    }
                    else //location.apply
                    {
                        userLatLng = LatLng(location.latitude, location.longitude)
                    }
                }
            }
        }*/

        initFilters()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            //poczatek funkcji map
            googleMap = it
            googleMap.isMyLocationEnabled = true

            fun getMarkers(): MutableList<Marker> {
                val markersJson = SharedPreferencesRepository.marker

                return if (markersJson != null)
                    Gson().fromJson(markersJson)
                else
                    mutableListOf()
            }

            fun saveMarker(m : Marker) {
                val markersTemp = getMarkers()
                markersTemp.add(m)
                SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
            }

            fun removeMarker(m: Marker) {
                val markersTemp = getMarkers()
                markersTemp.remove(m)
                SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
            }

            fun placeMarkerOnMap(m: Marker) {
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

            fun placeMarkersOfType(type: String) {
                val marOfType = getMarkers().filter {
                    it.type == type
                }
                for (m in marOfType) {
                    placeMarkerOnMap(m)
                }
            }

            val warszawa = LatLng(52.23, 21.01)
            val zoom = 20.0f

            fun refreshMap() {
                googleMap.clear()
                for((key, value) in filters) {
                    if(value) {
                        placeMarkersOfType(key)
                    }
                }
                //getLocation()
                println(userLatLng.latitude)
                println(userLatLng.longitude)
                //val s : String = userLatLng.latitude.toString() + ", " + userLatLng.longitude.toString()
                //Toast.makeText(this, s, Toast.LENGTH_LONG).show()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, zoom))
            }


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

            var action: String = ""

            var clickableMarkers = true
            var clickedMarker: Marker = defaultMarker

            val buttonConfirm = findViewById<Button>(R.id.Confirm)
            val buttonCancel = findViewById<Button>(R.id.Cancel)
            val backgroundButtons = findViewById<ImageView>(R.id.ButtonsBackground)
            backgroundButtons.isVisible = false
            buttonConfirm.isVisible = false
            buttonCancel.isVisible = false

            buttonConfirm.setOnClickListener {
                buttonCancel.isVisible = false
                buttonConfirm.isVisible = false
                backgroundButtons.isVisible = false
                clickableMarkers = true
                if(action == "delete") {
                    removeMarker(clickedMarker)
                    refreshMap()
                }
                else if(action == "add")
                {
                    saveMarker(clickedMarker)
                    setFilterValue("Trash", true)
                    refreshMap()
                }
            }

            buttonCancel.setOnClickListener {
                buttonCancel.isVisible = false
                buttonConfirm.isVisible = false
                backgroundButtons.isVisible = false
                clickableMarkers = true
            }


            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warszawa, zoom))
            //googleMap.uiSettings.setZoomControlsEnabled(true)
            val buttonAddMarker = findViewById<Button>(R.id.AddMarkerBtn)
            buttonAddMarker.setOnClickListener {
                if(clickableMarkers)
                {
                    val thisMarker = Marker(
                        "Trash",
                        userLatLng.latitude,
                        userLatLng.longitude,
                        false,
                        "user name"
                    )
                    action = "add"
                    buttonCancel.isVisible = true
                    buttonConfirm.isVisible = true
                    backgroundButtons.isVisible = true
                    clickableMarkers = false
                    clickedMarker = thisMarker
                }
            }

            /*googleMap.setOnMapClickListener {
                val thisMarker = Marker(
                    "Trash",
                    it.latitude,
                    it.longitude,
                    false,
                    "user name"
                )
                if(clickableMarkers) {
                    saveMarker(thisMarker)
                    setFilterValue("Trash", true)
                    refreshMap()
                }
            }*/

            googleMap.setOnMapLongClickListener {
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
            }

            googleMap.setOnMarkerClickListener { marker ->
                val thisMarker = Marker(
                    marker.title,
                    marker.position.latitude,
                    marker.position.longitude,
                    false,
                    "user name"
                )
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
                    } else if(marker.title == "Malysz") {
                        marker.showInfoWindow()
                    }
                }
                true
            }

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
                    buttonCancel.isVisible = true
                    buttonConfirm.isVisible = true
                    backgroundButtons.isVisible = true
                    clickableMarkers = false
                    clickedMarker = thisMarker
                }
            }

            refreshMap()
        }//koniec funkcji map

    }


}
