package com.hoffhaxx.app.concurs.misc

import android.util.Log
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository.aqi
import com.hoffhaxx.app.concurs.misc.data.PollutionData
import com.hoffhaxx.app.concurs.misc.data.Quest
import com.hoffhaxx.app.concurs.misc.data.QuestData
import com.hoffhaxx.app.concurs.web.PollutionClient
import com.hoffhaxx.app.concurs.web.WebClient

object QuestRepository {
    suspend fun getQuests(): MutableList<Quest>? {
        var quests : MutableList<Quest>?
        try {
            quests = WebClient.client.getQuests()?.quests
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
        return quests
    }

    suspend fun addQuests(quests : MutableList<Quest>) {
        try {
            WebClient.client.addQuests(quests)
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }
}