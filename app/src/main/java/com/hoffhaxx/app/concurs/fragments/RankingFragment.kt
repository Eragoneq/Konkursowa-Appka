package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository

/**
 * A simple [Fragment] subclass.
 */
class RankingFragment : Fragment() {

    lateinit var userLocationScore: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.ranking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLocationScore = view!!.findViewById(R.id.userLocationScore)
        setSmogScore()
    }

    private fun createURL() : String {
        val userLocation = SharedPreferencesRepository.userLocation
        if(userLocation != null) {
            val lat = kotlin.math.round(userLocation.latitude * 100) / 100
            val lng = kotlin.math.round(userLocation.longitude * 100) / 100
            return "https://api.waqi.info/feed/geo:$lat;$lng/?token=d62321aeafb151ae88c4a144aab1e9a962175263"
        }
        return ""
    }

    private fun setSmogScore()
    {
        var score = 67.toString()

        userLocationScore.text = score
    }

}

