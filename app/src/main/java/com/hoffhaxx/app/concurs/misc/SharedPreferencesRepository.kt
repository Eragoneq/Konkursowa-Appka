package com.hoffhaxx.app.concurs.misc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hoffhaxx.app.concurs.misc.data.Quest
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
    private val KEY_AQI = "aqi"
    private val KEY_QUESTS = "quests"

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
            val json = prefs.getString((KEY_USER_LOCATION), null) ?: return null
            return Gson().fromJson(json, UserLocation::class.java)
        }
        set(value) {
            val string = Gson().toJson(value)
            prefs.edit{ putString(KEY_USER_LOCATION, string) }
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
    var aqi : Int?
        get() = prefs.getInt(KEY_AQI, -1)
        set(value) = prefs.edit {
            if (value != null) {
                putInt(KEY_AQI, value)
            }
        }

    var quests : MutableList<Quest>?
        get() {
            val json = prefs.getString((KEY_QUESTS), null)
            if (json == null)
                return null
            val groupListType = object : TypeToken<MutableList<Quest>>() {}.type
            return Gson().fromJson(json, groupListType)
        }
        set(value) {
            val string = Gson().toJson(value)
            prefs.edit { putString(KEY_QUESTS, string) }
        }
}
