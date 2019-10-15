package com.hoffhaxx.app.concurs

data class Marker (val type: String,
                   val latitude: Double,
                   val longitude: Double,
                   val anonymous: Boolean,
                   val user: String){
}