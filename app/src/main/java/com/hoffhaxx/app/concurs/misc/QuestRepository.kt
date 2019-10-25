package com.hoffhaxx.app.concurs.misc

import android.util.Log
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository.aqi
import com.hoffhaxx.app.concurs.misc.data.PollutionData
import com.hoffhaxx.app.concurs.misc.data.Quest
import com.hoffhaxx.app.concurs.misc.data.QuestData
import com.hoffhaxx.app.concurs.web.PollutionClient
import com.hoffhaxx.app.concurs.web.WebClient

object QuestRepository {
    suspend fun getQuests(amount: Int): MutableList<Quest>? {
        try {
            var randompos : Int
            val quest_list = WebClient.client.getQuests()?.quests
            val quests = mutableListOf<Quest>()

            for (i in 1..amount){
                val questssize: Int = quest_list!!.size
                randompos = (0..questssize).random() - 1
                quests.add(quest_list[randompos])
            }
            SharedPreferencesRepository.quests = quests
            return quests
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }

    suspend fun addQuests(quests : MutableList<Quest>) {
        try {
            WebClient.client.addQuests(quests)
        } catch (e : retrofit2.HttpException) {
            throw WebClient.NetworkException()
        }
    }
}