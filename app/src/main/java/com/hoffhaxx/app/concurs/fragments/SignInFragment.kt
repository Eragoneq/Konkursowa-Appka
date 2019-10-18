package com.hoffhaxx.app.concurs.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoffhaxx.app.concurs.R


class SignInFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

// Load the animation like this

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
}
