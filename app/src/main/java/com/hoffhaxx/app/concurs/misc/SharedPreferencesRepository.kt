package com.hoffhaxx.app.concurs.misc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.hoffhaxx.app.concurs.misc.data.User
import com.hoffhaxx.app.concurs.misc.data.UserLocation

object SharedPreferencesRepository {
    private val PREFS_NAME = "hoffhaxx.preferences"
    private lateinit var prefs : SharedPreferences

    private val KEY_NAME = "name"
    private val KEY_MARKER = "marker"
    private val KEY_SESSION_ID = "sessionId"
    private val KEY_USER = "user"
    private val KEY_USER_LOCATION = "userLocation"

    fun initialize(context: Context) {
        if (!SharedPreferencesRepository::prefs.isInitialized)
            prefs = context.getSharedPreferences(
                PREFS_NAME, 0)
    }

    var name : String?
        get() = prefs.getString(KEY_NAME, null)
        set(value) = prefs.edit { putString(
            KEY_NAME, value) }

    var marker : String?
        get() = prefs.getString(KEY_MARKER, null)
        set(value) = prefs.edit { putString(
            KEY_MARKER, value) }

    var userLocation : UserLocation?
        get() {
            val json = prefs.getString((KEY_USER_LOCATION), null)
            if (json == null)
                return null
            return Gson().fromJson(json, UserLocation::class.java)
        }
        set(value) {
            val string = Gson().toJson(value)
            prefs.edit{ putString(KEY_USER, string) }
        }


    var sessionId : String?
        get() = prefs.getString(KEY_SESSION_ID, "")
        set(value) = prefs.edit { putString(KEY_SESSION_ID, value) }

    var user : User?
        get() {
            val json = prefs.getString((KEY_USER), null)
            if (json == null)
                return null
            return Gson().fromJson(json, User::class.java)
        }
        set(value) {
            val string = Gson().toJson(value)
            prefs.edit{ putString(KEY_USER, string) }
        }

}
