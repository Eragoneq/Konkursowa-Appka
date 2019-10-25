package com.hoffhaxx.app.concurs.misc

import com.hoffhaxx.app.concurs.activities.map.Marker
import com.hoffhaxx.app.concurs.web.WebClient

object MapRepository {
    suspend fun getMarkers(): List<Marker>? {
        var markers : List<Marker>?
        try {
            markers = WebClient.client.getMarkers()?.markers
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
        return markers
    }

    suspend fun addMarkers(markers : List<Marker>) {
        try {
            WebClient.client.addMarkers(markers)
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }
}