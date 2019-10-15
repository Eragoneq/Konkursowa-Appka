package com.hoffhaxx.app.concurs

import android.app.Application
import com.hoffhaxx.app.concurs.SharedPreferencesRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesRepository.initialize(this)
    }
}