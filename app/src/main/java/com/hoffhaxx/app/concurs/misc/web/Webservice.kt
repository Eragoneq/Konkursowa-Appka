package com.hoffhaxx.app.concurs.web

import com.hoffhaxx.app.concurs.misc.data.*
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

    @POST("/user/register")
    suspend fun userRegister(@Body body : RegisterCredentials) : SignUpResult
}