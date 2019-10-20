package com.hoffhaxx.app.concurs.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.fragments.*
import com.hoffhaxx.app.concurs.ui.PageTransformer

class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val fragmentsArray= listOf<Fragment>(InfoFragment(), AchievementsFragment(), HomepageFragment(), RankingFragment(), LeafFragment())

        viewPager = findViewById(R.id.viewPagerMain)
        bottomNav = findViewById(R.id.bottom_nav)

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        fragmentsArray.forEach { fragment: Fragment ->  pagerAdapter.addFragment(fragment) }

        //viewPager.setPageTransformer(false, PageTransformer())
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 2

        val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_nav_item_todo -> {
                    viewPager.setCurrentItem(0, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_item_achiev -> {
                    viewPager.setCurrentItem(1, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_menu_home -> {
                    viewPager.setCurrentItem(2, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_menu_stats -> {
                    viewPager.setCurrentItem(3, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_menu_leaf -> {
                    viewPager.setCurrentItem(4, false)
                    return@OnNavigationItemSelectedListener true
                }
            }
            true
        }

        bottomNav.setOnNavigationItemSelectedListener(navListener)
        bottomNav.selectedItemId = R.id.bottom_nav_menu_home

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = 2
        }
    }


}