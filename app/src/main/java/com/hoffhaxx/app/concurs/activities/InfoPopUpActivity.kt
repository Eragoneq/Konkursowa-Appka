package com.hoffhaxx.   app.concurs.activities

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.hoffhaxx.app.concurs.R

class InfoPopUpActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infopopup)

        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width: Int = dm.widthPixels
        var height: Int = dm.heightPixels

        window.setLayout((width*.8).toInt(), (height*.6).toInt())
    }
}