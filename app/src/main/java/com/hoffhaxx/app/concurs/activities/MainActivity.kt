package com.hoffhaxx.app.concurs.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.misc.UserRepository
import com.hoffhaxx.app.concurs.web.WebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{goToLogin()}

        displayUserName()



        fab.setOnClickListener { view ->
            Snackbar.make(view, "SiemaNie", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        logoutBtn.setOnClickListener {
            CoroutineScope(IO).launch {
                try {
                    UserRepository.logout()
                    withContext(Main) {
                        goToLogin()
                    }
                } catch (e : WebClient.NetworkException) {}
            }
        }
    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Log.println(Log.INFO, "TESTT", "klik")
        finish()
    }
    private fun displayUserName() = CoroutineScope(IO).launch {
        try {
            val user = UserRepository.getUser()
            withContext(Main) {
                if (user != null) {
                    Toast.makeText(this@MainActivity, "Zalogowano jako ${user.email}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e : WebClient.NetworkException) {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Błąd logwania")
                .setMessage("Nie można nawiązać połączenia z serwerem")
                .setNeutralButton("OK") {dialog, which ->  }
                .create()
                .show()
        }
    }
}
