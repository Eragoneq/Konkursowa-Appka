package com.hoffhaxx.app.concurs.web

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.misc.web.PollutionService
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


object PollutionClient {
    lateinit var connectivityManager : ConnectivityManager

    val host = "api.waqi.info"
    val port = "443"

    val okHttpClient = OkHttpClient
        .Builder()
        .addInterceptor{ chain ->
            try {
                if (!isConnected)
                    throw NoNetworkAvailableException()
                var request = chain.request()
                val url = request.url().newBuilder().addQueryParameter("token", "d62321aeafb151ae88c4a144aab1e9a962175263").build()
                request = request.newBuilder().url(url).build()
                return@addInterceptor chain.proceed(request)
            } catch (e : SocketTimeoutException) {
                throw TimeoutException()
            }
        }.build()

    val client = Retrofit.Builder()
        .baseUrl("https://$host:$port")
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build().create(PollutionService::class.java)

    private var isConnected : Boolean = false
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