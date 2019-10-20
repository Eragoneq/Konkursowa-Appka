package com.hoffhaxx.app.concurs.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.hoffhaxx.app.concurs.R

/**
 * A simple [Fragment] subclass.
 */
class HomepageFragment : Fragment() {

    lateinit var animation: Animation
    lateinit var img: ImageView

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
        img = view.findViewById<ImageView>(R.id.imageView3)
    }

    override fun getEnterTransition(): Any? {
        img.startAnimation(animation)
        return super.getEnterTransition()
    }

}
