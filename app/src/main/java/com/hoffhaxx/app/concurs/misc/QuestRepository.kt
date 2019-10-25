package com.hoffhaxx.app.concurs.misc

import com.hoffhaxx.app.concurs.misc.data.Quest
import com.hoffhaxx.app.concurs.misc.web.WebClient

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