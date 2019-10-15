package com.hoffhaxx.app.concurs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    private val filters = HashMap<String, Boolean>()

    private fun initFilters()
    {
        filters["Trash"] = true
        filters["Malysz"] = false
    }

    lateinit var mapFragment: SupportMapFragment
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
            checkBoxTrash.isChecked = true //domyslne
            checkBoxTrash?.setOnCheckedChangeListener { buttonView, isChecked ->
                filters["Trash"] = isChecked
                refreshMap()
            }
            val checkBoxMalysz = findViewById<CheckBox>(R.id.Malysz)
            checkBoxMalysz.isChecked = false //domyslne
            checkBoxMalysz?.setOnCheckedChangeListener { buttonView, isChecked ->
                filters["Malysz"] = isChecked
                refreshMap()
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
                val thisMarker = Marker("Trash", it.latitude, it.longitude, false, "user name")
                saveMarker(thisMarker)
                setFilterValue("Trash", true)
                refreshMap()
            }

            googleMap.setOnMapLongClickListener {
                val thisMarker = Marker("Malysz", it.latitude, it.longitude, false, "user name")
                saveMarker(thisMarker)
                setFilterValue("Malysz", true)
                refreshMap()
            }

            googleMap.setOnMarkerClickListener { marker ->
                if(marker.title == "Trash")
                {
                    val thisMarker = Marker(marker.title, marker.position.latitude, marker.position.longitude, false, "user name")
                    removeMarker(thisMarker)
                    refreshMap()
                }
                else if(marker.title == "Malysz")
                {
                    val thisMarker = Marker(marker.title, marker.position.latitude, marker.position.longitude, false, "user name")
                    removeMarker(thisMarker)
                    refreshMap()
                }
            true
            }

            refreshMap()
        })//koniec funkcji map

    }


}
