package com.hoffhaxx.app.concurs

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{goToLogin()}
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "SiemaNie", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Log.println(Log.INFO, "TEST", "klik")
        finish()
    }
}
