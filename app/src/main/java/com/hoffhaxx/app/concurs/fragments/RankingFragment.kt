package com.hoffhaxx.app.concurs.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.activities.MapActivity
import com.hoffhaxx.app.concurs.misc.PollutionRepository
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.misc.web.WebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class RankingFragment : Fragment() {

    private lateinit var userLocationScore: TextView
    private lateinit var userLocationBackground: ImageView
    private lateinit var mapButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.ranking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLocationScore = view.findViewById(R.id.userLocationScore)
        userLocationBackground = view.findViewById(R.id.userLocationBackground)
        userLocationBackground.setOnClickListener { setPollutionScore() }
        mapButton = view.findViewById(R.id.mapButton)
        mapButton.setOnClickListener { goToMap() }

        setPollutionScore()
    }

    private fun setPollutionScore() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val userLocation = SharedPreferencesRepository.userLocation
            withContext(Main) {
                if (userLocation != null) {
                    val lat = kotlin.math.round(userLocation.latitude * 100) / 100
                    val lng = kotlin.math.round(userLocation.longitude * 100) / 100
                    val result = PollutionRepository.getAqi(lat, lng)
                    val s = result!!.data.aqi.toString()
                    userLocationScore.text = s
                    when {
                        result.data.aqi <= 50 -> userLocationBackground.background =
                            ResourcesCompat.getDrawable(resources, R.color.pollution50Color, null)
                        result.data.aqi <= 100 -> userLocationBackground.background =
                            ResourcesCompat.getDrawable(resources, R.color.pollution100Color, null)
                        result.data.aqi <= 150 -> userLocationBackground.background =
                            ResourcesCompat.getDrawable(resources, R.color.pollution150Color, null)
                        result.data.aqi <= 200 -> userLocationBackground.background =
                            ResourcesCompat.getDrawable(resources, R.color.pollution200Color, null)
                        result.data.aqi <= 300 -> userLocationBackground.background =
                            ResourcesCompat.getDrawable(resources, R.color.pollution300Color, null)
                        else -> userLocationBackground.background =
                            ResourcesCompat.getDrawable(resources, R.color.pollution300Color, null)
                    }
                } else {
                    userLocationScore.text = getString(R.string.no_data)
                    userLocationBackground.background =
                        ResourcesCompat.getDrawable(resources, R.color.pollutionDefault, null)
                }
            }
        } catch (e : WebClient.NetworkException) {

        } catch (e: IllegalStateException){
            Log.println(Log.ERROR, "FRAG", "Ranking element is not available")
        }
    }


    /*private fun setPollutionScore() {
        var score = 67.toString()

        userLocationScore.text = score
    }*/

    private fun goToMap(){
        val intent = Intent(this.context, MapActivity::class.java)
        startActivity(intent)
    }

}

