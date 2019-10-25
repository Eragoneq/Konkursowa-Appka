package com.hoffhaxx.app.concurs.misc

import com.hoffhaxx.app.concurs.misc.data.Quest
import com.hoffhaxx.app.concurs.misc.web.WebClient

object QuestRepository {
    suspend fun getQuests(amount: Int): MutableList<Quest>? {
        try {
            var randompos : Int
            val quest_list = WebClient.client.getQuests()?.quests
            val quests = mutableListOf<Quest>()
            var questssize: Int
            if (quest_list != null)
            {
                questssize = quest_list.size
                for (i in 1..amount){
                    val questssize: Int = quest_list!!.size
                    randompos = (0..questssize).random() - 1
                    quests.add(quest_list[randompos])
                }
            }else{
                questssize = 0
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