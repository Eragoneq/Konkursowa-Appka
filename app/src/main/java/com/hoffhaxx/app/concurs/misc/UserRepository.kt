package com.hoffhaxx.app.concurs.misc

import com.hoffhaxx.app.concurs.misc.data.*
import com.hoffhaxx.app.concurs.misc.web.WebClient

object UserRepository {

    suspend fun getUser(): User? {
        var user = SharedPreferencesRepository.user
        if (SharedPreferencesRepository.sessionId != "") {
            try {
                user =  WebClient.client.userProfile()
                SharedPreferencesRepository.user = user
            } catch (e : retrofit2.HttpException) {
                throw WebClient.NetworkException()
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
            throw WebClient.NetworkException()
        }
    }

    suspend fun googleAuth(idToken : String) : SignInResult {
        return try {
            WebClient.client.userGoogleAuth(LoginOauthGoogleCredentials(idToken))
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }

    suspend fun logout() {
        try {
            WebClient.client.userLogout()
            SharedPreferencesRepository.user = null
            SharedPreferencesRepository.sessionId = ""
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }

    suspend fun register(nickname: String, email: String, password: String): SignUpResult {
        return try {
            WebClient.client.userRegister(RegisterCredentials(nickname, email, password))
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }
}