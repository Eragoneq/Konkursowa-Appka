package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository

/**
 * A simple [Fragment] subclass.
 */
class RankingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val userlocationScore: TextView = view!!.findViewById(R.id.userLocationScore)
        fun setSmogScore()
        {
            
            //userlocationScore.text =
        }

        return inflater.inflate(R.layout.ranking_fragment, container, false)
    }

    private fun createURL() : String {
        val userLocation = SharedPreferencesRepository.userLocation
        return "https://api.waqi.info/feed/geo:${userLocation!!.latitude};${userLocation!!.longitude}/?token=d62321aeafb151ae88c4a144aab1e9a962175263"
    }

}

