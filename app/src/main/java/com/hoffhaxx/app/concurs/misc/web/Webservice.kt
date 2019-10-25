package com.hoffhaxx.app.concurs.misc.web

import com.hoffhaxx.app.concurs.activities.map.Marker
import com.hoffhaxx.app.concurs.misc.data.*
import retrofit2.http.*

interface Webservice {
    @GET("/user/profile")
    suspend fun userProfile() : User?

    @POST("/user/login")
    suspend fun userLoginLocal(@Body body : LoginCredentials) : SignInResult

    @POST("/user/oauth/google")
    suspend fun userGoogleAuth(@Body body : LoginOauthGoogleCredentials) : SignInResult

    @POST("/user/logout")
    suspend fun userLogout()

    @POST("/user/register")
    suspend fun userRegister(@Body body : RegisterCredentials) : SignUpResult

    @GET("/quest")
    suspend fun getQuests() : QuestData?

    @POST("/quest")
    suspend fun addQuests(@Body body : MutableList<Quest>)

    @GET("/map")
    suspend fun getMarkers() : MarkerData?

    @POST("/map")
    suspend fun addMarkers(@Body body : MutableList<Marker>)

    @DELETE("/map/{id}")
    suspend fun removeMarkers(@Path("id") id : String)
}