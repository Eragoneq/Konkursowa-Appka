package com.hoffhaxx.app.concurs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapterAchievementsFragment(private val achievements_cards: ArrayList<AchievementCard>):
    RecyclerView.Adapter<RecyclerViewAdapterAchievementsFragment.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapterAchievementsFragment.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_achievementsfragment,
            parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return achievements_cards.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapterAchievementsFragment.ViewHolder, position: Int) {
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