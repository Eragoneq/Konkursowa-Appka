package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.misc.SharedPreferencesRepository
import com.hoffhaxx.app.concurs.misc.UserRepository

/**
 * A simple [Fragment] subclass.
 */
class HomepageFragment : Fragment() {

    lateinit var animation: Animation
    lateinit var img: ImageView
    lateinit var lvl: TextView
    lateinit var welcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.homepage_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation = AnimationUtils.loadAnimation(this.context, R.anim.fade_in_anim)
        img = view.findViewById(R.id.lvlRectangle)
        lvl = view.findViewById(R.id.user_level)
        welcome = view.findViewById(R.id.welcome_back)

        lvl.text = "0" //Ustawienie levela
        welcome.text = welcome.text.toString().format(SharedPreferencesRepository.user!!.nickname)
    }

//    override fun onResume() {
//        super.onResume()
//        img.startAnimation(animation)
//    }

}
