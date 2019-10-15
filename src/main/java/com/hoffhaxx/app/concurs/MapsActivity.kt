package com.hoffhaxx.app.concurs

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.jar.Manifest
import com.google.gson.Gson

class MapsActivity() : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //Lokacja usera

    /*fun checkPermission()
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), AccessLocation)
                return
            }
        }

        getUserLocation()
    }

    fun getUserLocation()
    {
        Toast.makeText(this, "UserLocation Access On", Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            AccessLocation -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getUserLocation()
                }
                else
                {
                    Toast.makeText(this, "User Not Granted Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    */
    //koniec lokacja usera

    private fun getMarkers(): MutableList<Marker> {
        var markersJson = SharedPreferencesRepository.marker

        return if (markersJson != null)
            Gson().fromJson(markersJson)
        else
            mutableListOf()
    }

    private fun saveMarker(m : Marker)
    {
        val markersTemp = getMarkers()
        markersTemp.add(m)
        SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
    }

    private fun placeMarkerOnMap(m: Marker) {
        val location = LatLng(m.latitude, m.longitude)
        if(m.type == "Trash")
        {
            mMap.addMarker(MarkerOptions()
                .position(location)
                .title(m.type)
                .snippet(m.user)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                .visible(true)
            )
        }
        else if(m.type == "Malysz")
        {
            mMap.addMarker(MarkerOptions()
                .position(location)
                .title(m.type)
                .snippet(m.user)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.malysz))
                .visible(true)
            )
        }
    }

    private fun placeMarkersOfType(type: String)
    {
        val marOfType = getMarkers().filter {
            it.type == type
        }
        for (m in marOfType)
        {
            placeMarkerOnMap(m)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val warszawa = LatLng(52.23, 21.01)
        val zoom = 16.0f
        //placeMarkerOnMap(warszawa)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warszawa, zoom))
        mMap.uiSettings.setZoomControlsEnabled(true)

        mMap.setOnMapClickListener {
            var thisMarker = Marker("Trash", it.latitude, it.longitude, false, "user name")
            saveMarker(thisMarker)
            mMap.clear()
            placeMarkersOfType("Trash")
        }

        mMap.setOnMapLongClickListener {
            var thisMarker = Marker("Malysz", it.latitude, it.longitude, false, "user name")
            saveMarker(thisMarker) /*Ten fragment wywala*/
            mMap.clear()
            placeMarkersOfType("Malysz")
        }

        /*mMap.setOnMarkerClickListener { marker ->
            if(marker.title == "Trash")
            {
                mMap.clear()
            }
        true
        }*/
    }
}
