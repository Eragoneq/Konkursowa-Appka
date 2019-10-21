package com.hoffhaxx.app.concurs.activities.map

data class Marker (val type: String,
                   val latitude: Double,
                   val longitude: Double,
                   val anonymous: Boolean,
                   val user: String){
}