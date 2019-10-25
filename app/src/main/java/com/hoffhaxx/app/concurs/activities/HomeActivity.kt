package com.hoffhaxx.app.concurs.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.activities.map.Marker
import com.hoffhaxx.app.concurs.fragments.*
import com.hoffhaxx.app.concurs.misc.MapRepository
import com.hoffhaxx.app.concurs.misc.PollutionRepository
import com.hoffhaxx.app.concurs.misc.QuestRepository
import com.hoffhaxx.app.concurs.misc.data.Quest
import com.hoffhaxx.app.concurs.web.WebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var bottomNav: BottomNavigationView

    private fun testPollution() = CoroutineScope(Dispatchers.IO).launch {
        try {
//            val markers = MapRepository.addMarkers(mutableListOf(Marker("malysz", 1.3, 1.5, "twojstary", "dzisiaj", "idtxd")))
            //MapRepository.removeMarker("5db332a680fe726923b85d13")
//            Log.i("TESTxddd", markers.toString())
        } catch (e : WebClient.NetworkException) {
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(this@HomeActivity)
                    .setTitle(getString(R.string.logging_error))
                    .setMessage(getString(R.string.cannot_connect_to_server))
                    .setNeutralButton(getString(R.string.ok)) {dialog, which ->  }
                    .create()
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        testPollution()

        val fragmentsArray= listOf(InfoFragment(), AchievementsFragment(), HomepageFragment(), RankingFragment(), LeafFragment())

        viewPager = findViewById(R.id.viewPagerMain)
        bottomNav = findViewById(R.id.bottom_nav)

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        fragmentsArray.forEach { fragment: Fragment ->  pagerAdapter.addFragment(fragment) }

        //viewPager.setPageTransformer(false, PageTransformer())
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 2
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0->bottomNav.selectedItemId = R.id.bottom_nav_menu_todo
                    1->bottomNav.selectedItemId = R.id.bottom_nav_menu_achiev
                    2->bottomNav.selectedItemId = R.id.bottom_nav_menu_home
                    3->bottomNav.selectedItemId = R.id.bottom_nav_menu_stats
                    4->bottomNav.selectedItemId = R.id.bottom_nav_menu_leaf
                }
            }
        })

        val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_nav_menu_todo -> {
                    viewPager.setCurrentItem(0, false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_menu_achiev -> {
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
        if (viewPager.currentItem == 2) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = 2
        }
    }

}