package com.hoffhaxx.app.concurs.misc

import android.util.Log
import com.hoffhaxx.app.concurs.misc.data.LoginCredentials
import com.hoffhaxx.app.concurs.misc.data.LoginOauthGoogleCredentials
import com.hoffhaxx.app.concurs.misc.data.SignInResult
import com.hoffhaxx.app.concurs.misc.data.User
import com.hoffhaxx.app.concurs.misc.web.WebClient

object UserRepository {
    suspend fun getUser(): User? {
        var user = SharedPreferencesRepository.user
        if (user != null)
            return user
        val sessionId = SharedPreferencesRepository.sessionId
        if (sessionId != null) {
            try {
                user = WebClient.client.userProfile()
            } catch (e : retrofit2.HttpException) {
                return null
            }
            if (user != null)
                return user
        }
        return null
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
        try {
            val result = WebClient.client.userGoogleAuth(LoginOauthGoogleCredentials(idToken))
            return result
        } catch (e : retrofit2.HttpException) {
            Log.i("SOOMETHING", e.response().toString())
            return SignInResult(success = false, message = "Network error")
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