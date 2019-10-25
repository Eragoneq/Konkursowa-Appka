package com.hoffhaxx.app.concurs.misc

import android.util.Log
import com.hoffhaxx.app.concurs.misc.data.PollutionData
import com.hoffhaxx.app.concurs.misc.web.PollutionClient
import com.hoffhaxx.app.concurs.misc.web.WebClient

object PollutionRepository {
    suspend fun getAqi(latitude : Double, longitude : Double): PollutionData? {
        var aqi : PollutionData? = null
        try {
            aqi =  PollutionClient.client.getAqi("geo:${latitude};${longitude}")
            Log.i("TEST", aqi.toString())
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
        return aqi
    }
}