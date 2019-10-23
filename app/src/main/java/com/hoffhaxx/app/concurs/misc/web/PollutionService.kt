package com.hoffhaxx.app.concurs.misc.web

import com.hoffhaxx.app.concurs.misc.data.PollutionData
import com.hoffhaxx.app.concurs.misc.data.User
import retrofit2.http.GET
import retrofit2.http.Path

interface PollutionService {
    @GET("/feed/{geo}/")
    suspend fun getAqi(@Path("geo") geo : String) : PollutionData
}