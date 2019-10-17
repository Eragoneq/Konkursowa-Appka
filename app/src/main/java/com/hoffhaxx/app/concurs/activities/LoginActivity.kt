package com.hoffhaxx.app.concurs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.activities.auth.SignIn
import com.hoffhaxx.app.concurs.activities.auth.SignInGoogle
import com.hoffhaxx.app.concurs.activities.auth.SignUp


class LoginActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        setupBottomMargin()

        val signIn: Button = findViewById(R.id.sign_in_button)
        val signInGoogle: Button = findViewById(R.id.sign_in_google)
        val signUp: Button = findViewById(R.id.sign_up)


    }

    private fun signIn(){
        // Tutaj powinien isc kod sprawdzajacy sesje
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)
        finish()
    }

    private fun signInGoogle(){
        val intent = Intent(this, SignInGoogle::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUp(){
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }

    private fun getSoftButtonsBarSizePort(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight)
            realHeight - usableHeight
        else
            0
    }

    private fun setupBottomMargin(){
        val forgotBtn: View = findViewById(R.id.sign_in_dont_rembmer_password_btn)
        val param = forgotBtn.layoutParams as ConstraintLayout.LayoutParams
        param.setMargins(0,0,0, getSoftButtonsBarSizePort(this))
        forgotBtn.layoutParams = param
    }
}