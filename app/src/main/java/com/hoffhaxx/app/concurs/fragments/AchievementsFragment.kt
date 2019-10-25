package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoffhaxx.app.concurs.AchievementCard
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.RecyclerViewAdapter_AchievementsFragment
import kotlinx.android.synthetic.main.achievements_fragment.*

/**
 * A simple [Fragment] subclass.
 */
private val achievementCards = arrayListOf(
    AchievementCard("Achievement1", R.drawable.ic_achievements),
    AchievementCard("Achievement2", R.drawable.ic_achievements),
    AchievementCard("Achievement3", R.drawable.ic_achievements),
    AchievementCard("Achievement4", R.drawable.ic_achievements)
)

class AchievementsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.achievements_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewAchievements.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter_AchievementsFragment(achievementCards)
        }
    }


}
