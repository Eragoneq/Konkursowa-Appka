package com.hoffhaxx.app.concurs.misc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesRepository {
    private val PREFS_NAME = "hoffhaxx.preferences"
    private lateinit var prefs : SharedPreferences

    private val KEY_NAME = "name"
    private val KEY_MARKER = "marker"

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

}
