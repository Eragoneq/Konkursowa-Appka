package com.hoffhaxx.app.concurs.activities.map

import java.util.*

data class Marker (val type: String,
                   val latitude: Double,
                   val longitude: Double,
                   val addedBy: String,
                   val addedDate: String,
                   val id: String)