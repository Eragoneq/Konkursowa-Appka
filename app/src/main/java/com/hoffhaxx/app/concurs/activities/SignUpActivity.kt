package com.hoffhaxx.app.concurs.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.misc.UserRepository
import com.hoffhaxx.app.concurs.web.WebClient
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        signUp.setOnClickListener {
            val nickname = nickname_edit_signup.text.toString()
            val email = mail.text.toString()
            val password = pass.text.toString()
            signUp(nickname, email, password)
        }
        mail.setOnFocusChangeListener { _, hasFocus -> if(!hasFocus) testMail() }
        pass.setOnFocusChangeListener { _, hasFocus -> if(!hasFocus) testPass() }
        goto_sign_in_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUp(nickname : String, email : String, password : String) = CoroutineScope(IO).launch {
        try {
            val result = UserRepository.register(nickname, email, password)
            withContext(Main) {
                if (result.success)
                    goToMain()
                else {
                    val errors = result.errors
                    if (errors.isNotEmpty()) {
                        for (error in errors) {
                            when(error.param) {
                                "nickname" -> sign_up_text_input_nickname.error = error.message
                                "email" -> sign_up_text_input_email.error = error.message
                                "password" -> sign_up_text_input_password.error = error.message
                            }
                        }
                    }
                }
            }
        } catch (e : WebClient.NetworkException) {
            withContext(Main) {
                AlertDialog.Builder(this@SignUpActivity)
                    .setTitle("Błąd logwania")
                    .setMessage("Nie można nawiązać połączenia z serwerem")
                    .setNeutralButton("OK") {dialog, which ->  }
                    .create()
                    .show()
            }
        }
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