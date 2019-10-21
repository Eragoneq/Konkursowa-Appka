package com.hoffhaxx.app.concurs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        viewPager = findViewById(R.id.viewPagerMain)

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(InfoFragment())
        pagerAdapter.addFragment(AchievementsFragment())
        pagerAdapter.addFragment(HomepageFragment())
        pagerAdapter.addFragment(RankingFragment())
        pagerAdapter.addFragment(LeafFragment())

        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 2
    }


}