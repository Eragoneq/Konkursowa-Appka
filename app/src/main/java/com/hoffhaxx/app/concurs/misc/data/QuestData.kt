package com.hoffhaxx.app.concurs.misc.data

data class Quest(val description: String, val points: Double)

data class QuestData(val result: String, val quests: MutableList<Quest>)