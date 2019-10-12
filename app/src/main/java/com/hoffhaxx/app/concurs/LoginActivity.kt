package com.hoffhaxx.app.concurs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val signButton: Button = findViewById(R.id.signInButton)

        signButton.setOnClickListener{signIn()}
    }

    private fun signIn(){

        // Tutaj powinien isc kod sprawdzajacy sesje

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}