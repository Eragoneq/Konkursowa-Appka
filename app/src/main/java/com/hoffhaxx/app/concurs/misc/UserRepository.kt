package com.hoffhaxx.app.concurs.misc

import android.util.Log
import com.hoffhaxx.app.concurs.misc.data.LoginCredentials
import com.hoffhaxx.app.concurs.misc.data.LoginOauthGoogleCredentials
import com.hoffhaxx.app.concurs.misc.data.SignInResult
import com.hoffhaxx.app.concurs.misc.data.User
import com.hoffhaxx.app.concurs.web.WebClient

object UserRepository {
    suspend fun getUser(): User? {
        var user = SharedPreferencesRepository.user
        if (SharedPreferencesRepository.sessionId != "") {
            try {
                user =  WebClient.client.userProfile()
            } catch (e : retrofit2.HttpException) {
                Log.i("SOMETHING", e.message())
            }
        }
        return user
    }
    suspend fun loginUserLocal(email: String, password : String) : SignInResult {
        val credentials = LoginCredentials(email, password)
        SharedPreferencesRepository.sessionId = ""
        return try {
            WebClient.client.userLoginLocal(credentials)
        } catch (e : retrofit2.HttpException) {
            Log.i("SOOMETHING", e.response().toString())
            SignInResult(success = false, message = "Network error")
        }
    }

    suspend fun googleAuth(idToken : String) : SignInResult {
        return try {
            WebClient.client.userGoogleAuth(LoginOauthGoogleCredentials(idToken))
        } catch (e : retrofit2.HttpException) {
            Log.i("SOOMETHING", e.response().toString())
            SignInResult(success = false, message = "Network error")
        }
    }

    suspend fun logout() {
        try {
            WebClient.client.userLogout()
            SharedPreferencesRepository.user = null
            SharedPreferencesRepository.sessionId = ""
        } catch (e : retrofit2.HttpException) {
            Log.i("SOOMETHING", e.response().toString())
        }
    }
}