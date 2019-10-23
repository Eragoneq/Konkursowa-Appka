package com.hoffhaxx.app.concurs.misc.data

data class Data(val aqi : Int)

data class PollutionData(val status : String, val data : Data)