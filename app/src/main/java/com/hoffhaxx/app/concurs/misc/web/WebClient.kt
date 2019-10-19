package com.hoffhaxx.app.concurs.web

import android.util.Log
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.Cookie
import okhttp3.HttpUrl




object WebClient {
    val host = "10.0.2.2"
    val port = "8080"
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
        }).addInterceptor { chain ->
            val response = chain.proceed(chain.request())
            if (response.code() == 401)
                SharedPreferencesRepository.sessionId = ""
            return@addInterceptor response
        }.build()

    val client = Retrofit.Builder()
        .baseUrl("http://$host:$port")
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build().create(Webservice::class.java)
}