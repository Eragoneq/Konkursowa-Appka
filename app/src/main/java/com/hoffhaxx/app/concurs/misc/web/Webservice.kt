package com.hoffhaxx.app.concurs.web

import com.hoffhaxx.app.concurs.misc.data.LoginCredentials
import com.hoffhaxx.app.concurs.misc.data.LoginOauthGoogleCredentials
import com.hoffhaxx.app.concurs.misc.data.SignInResult
import com.hoffhaxx.app.concurs.misc.data.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Webservice {
    @GET("/user/profile")
    suspend fun userProfile() : User?

    @POST("/user/login")
    suspend fun userLoginLocal(@Body body : LoginCredentials) : SignInResult

    @POST("/user/oauth/google")
    suspend fun userGoogleAuth(@Body body : LoginOauthGoogleCredentials) : SignInResult

    @POST("/user/logout")
    suspend fun userLogout()
}