package com.hoffhaxx.app.concurs.misc.web

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.IOException
import java.net.SocketTimeoutException


object WebClient {
    lateinit var connectivityManager : ConnectivityManager

    val host = "fajnaaplikacja.pl"
    val port = "443"

    val okHttpClient = OkHttpClient
        .Builder()
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                if (url.host() == host) {
                    for (cookie in cookies) {
                        if (cookie.name() == "connect.sid") {
                            SharedPreferencesRepository.sessionId = cookie.value()
                            break
                        }
                    }
                }
            }
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                var list = ArrayList<Cookie>()
                if (url.host() == host) {
                    val sessionId = SharedPreferencesRepository.sessionId
                    if (sessionId != "") {
                        val cookie = Cookie.parse(url, "connect.sid=$sessionId")
                        cookie?.let { list.add(it) }
                    }
                }
                return list;
            }
        }).addInterceptor{ chain ->
            try {
                if (!isConnected)
                    throw NoNetworkAvailableException()
                val response = chain.proceed(chain.request())
                if (response.code() == 401)
                    SharedPreferencesRepository.sessionId = ""
                return@addInterceptor response
            } catch (e : SocketTimeoutException) {
                throw TimeoutException()
            }
        }.build()

    val client = Retrofit.Builder()
        .baseUrl("https://$host:$port")
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build().create(Webservice::class.java)

    var isConnected : Boolean = false
        get() {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)

            return connection != null && (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }
    fun initialize(context : Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    open class NetworkException : IOException()
    class NoNetworkAvailableException : NetworkException()
    class TimeoutException : NetworkException()
}