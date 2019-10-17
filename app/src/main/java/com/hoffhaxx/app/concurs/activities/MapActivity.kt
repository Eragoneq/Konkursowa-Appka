package com.hoffhaxx.app.concurs.activities

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

class MapActivity : AppCompatActivity() {

    private val filters = HashMap<String, Boolean>()

    private fun initFilters()
    {
        filters["Trash"] = true
        filters["Malysz"] = false
    }
    
    private var isMarkerClicked: Boolean = false
    private val defaultMarker: Marker =
        Marker("", 0.0, 0.0, false, "")
    private var lastClickedMarker: Marker = defaultMarker

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        initFilters()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            //poczatek funkcji map
            googleMap = it
            //googleMap.isMyLocationEnabled = true

            fun getMarkers(): MutableList<Marker> {
                val markersJson = SharedPreferencesRepository.marker

                return if (markersJson != null)
                    Gson().fromJson(markersJson)
                else
                    mutableListOf()
            }

            fun saveMarker(m : Marker)
            {
                val markersTemp = getMarkers()
                markersTemp.add(m)
                SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
            }

            fun removeMarker(m: Marker)
            {
                val markersTemp = getMarkers()
                markersTemp.remove(m)
                SharedPreferencesRepository.marker = Gson().toJson(markersTemp)
            }

            fun placeMarkerOnMap(m: Marker) {
                val location = LatLng(m.latitude, m.longitude)
                if(m.type == "Trash")
                {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(m.type)
                            .snippet(m.user)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker32))
                            .visible(true)
                    )
                }
                else if(m.type == "Malysz")
                {
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

            fun placeMarkersOfType(type: String)
            {
                val marOfType = getMarkers().filter {
                    it.type == type
                }
                for (m in marOfType)
                {
                    placeMarkerOnMap(m)
                }
            }

            fun refreshMap()
            {
                googleMap.clear()
                for((key, value) in filters)
                {
                    if(value)
                    {
                        placeMarkersOfType(key)
                    }
                }
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
                removeMarker(clickedMarker)
                refreshMap()
            }

            buttonCancel.setOnClickListener {
                buttonCancel.isVisible = false
                buttonConfirm.isVisible = false
                backgroundButtons.isVisible = false
                clickableMarkers = true
            }

            fun setFilterValue(type: String, value: Boolean)
            {
                filters[type] = value
                if(type == "Trash") {
                    checkBoxTrash.isChecked = value
                }else if(type == "Malysz"){
                    checkBoxMalysz.isChecked = value
                }
            }

            val warszawa = LatLng(52.23, 21.01)
            val zoom = 16.0f
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warszawa, zoom))
            googleMap.uiSettings.setZoomControlsEnabled(true)

            googleMap.setOnMapClickListener {
                val thisMarker = Marker(
                    "Trash",
                    it.latitude,
                    it.longitude,
                    false,
                    "user name"
                )
                if(clickableMarkers)
                {
                    saveMarker(thisMarker)
                    setFilterValue("Trash", true)
                    refreshMap()
                }
            }

            googleMap.setOnMapLongClickListener {
                val thisMarker = Marker(
                    "Malysz",
                    it.latitude,
                    it.longitude,
                    false,
                    "user name"
                )
                if(clickableMarkers)
                {
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
                if(clickableMarkers)
                {
                    if(marker.title == "Trash")
                    {
                        if(!isMarkerClicked || lastClickedMarker != thisMarker)
                        {
                            marker.showInfoWindow()
                            lastClickedMarker = thisMarker
                            isMarkerClicked = true
                        }
                        else
                        {
                            isMarkerClicked = false
                            lastClickedMarker = defaultMarker
                        }
                    }
                    else if(marker.title == "Malysz")
                    {
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
                if(marker.title == "Trash")
                {
                    buttonCancel.isVisible = true
                    buttonConfirm.isVisible = true
                    backgroundButtons.isVisible = true
                    clickableMarkers = false
                    clickedMarker = thisMarker
                }
            }

            refreshMap()
        })//koniec funkcji map

    }


}
