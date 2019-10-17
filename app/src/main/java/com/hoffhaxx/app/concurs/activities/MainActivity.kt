package com.hoffhaxx.app.concurs.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.hoffhaxx.app.concurs.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{goToLogin()}
        fab.setOnClickListener { view ->
            Snackbar.make(view, "SiemaNie", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Log.println(Log.INFO, "TEST", "klik")
        finish()
    }
}
