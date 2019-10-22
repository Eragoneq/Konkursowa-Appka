package com.hoffhaxx.app.concurs

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter_AchievementsFragment(private val achievements_cards: ArrayList<AchievementCard>):
    RecyclerView.Adapter<RecyclerViewAdapter_AchievementsFragment.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter_AchievementsFragment.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_achievementsfragment,
            parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return achievements_cards.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter_AchievementsFragment.ViewHolder, position: Int) {
        val achievementscard : AchievementCard = achievements_cards[position]
        holder.textViewAchievemtnInfo?.text = achievementscard.achievementInfo
        holder.imageViewAchievement?.setImageResource(achievementscard.achievementImageId)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textViewAchievemtnInfo:TextView? = null
        var imageViewAchievement:ImageView? = null

        init {
            textViewAchievemtnInfo = itemView.findViewById(R.id.recyclerView_textAchievementInfo)
            imageViewAchievement= itemView.findViewById(R.id.recyclerView_imageAchievement)
        }
    }
}