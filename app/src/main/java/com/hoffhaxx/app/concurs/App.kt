package com.hoffhaxx.app.concurs

import android.app.Application
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.web.PollutionClient
import com.hoffhaxx.app.concurs.web.WebClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesRepository.initialize(this)
        WebClient.initialize(this)
        PollutionClient.initialize(this)
    }
}