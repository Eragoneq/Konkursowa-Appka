package com.hoffhaxx.app.concurs.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.hoffhaxx.app.concurs.R

class SignUpActivity: AppCompatActivity() {

    private lateinit var pass: TextInputEditText
    private lateinit var mail: TextInputEditText
    private lateinit var errText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        val signUp: Button = findViewById(R.id.sign_up_button)
        pass = findViewById(R.id.password_edit_signup)
        mail = findViewById(R.id.email_edit_signup)
        errText = findViewById(R.id.error_text_signup)

        signUp.setOnClickListener {  }
        mail.setOnFocusChangeListener { _, hasFocus -> if(!hasFocus) testMail() }
        pass.setOnFocusChangeListener { _, hasFocus -> if(!hasFocus) testPass() }
    }

    private fun signUp(){
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun testMail(){
        errText.text = if (!isEmailValid(mail.text.toString())) resources.getString(R.string.illegal_email)  else  ""
    }

    private fun testPass(){
        if (pass.text.toString().length < 5){
            errText.text = resources.getString(R.string.illegal_pass)
        }else{
            errText.text = ""
        }
    }
}