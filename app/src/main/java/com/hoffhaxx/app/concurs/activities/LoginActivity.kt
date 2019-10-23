package com.hoffhaxx.app.concurs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hoffhaxx.app.concurs.R
import com.hoffhaxx.app.concurs.fragments.SignUpFragment
import com.hoffhaxx.app.concurs.misc.UserRepository
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity: AppCompatActivity(){

    lateinit var mGoogleSignInClient : GoogleSignInClient
    private var RC_SIGN_IN = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        setupBottomMargin()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("765380157329-qkpmq9v5f32m71ca3vopi6m031s2q69u.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signIn: Button = findViewById(R.id.sign_in_button)
        val signInGoogle: Button = findViewById(R.id.sign_in_google)
        val signUp: Button = findViewById(R.id.sign_up)

//        CoroutineScope(IO).launch {
//            val user = UserRepository.getUser()
//            if (user?.email != null) {
//                Log.i("XD", user.toString())
//                goToMain()
//            }
//        }
//        signIn.setOnClickListener { signIn() }
        signInGoogle.setOnClickListener { googleSignIn() }
        signUp.setOnClickListener{ signUp() }
        signIn.setOnClickListener { goToHomePage() }
    }

    //signIn() zamienione na goToMain
    private fun goToMain(){
        // Tutaj powinien isc kod sprawdzajacy sesje
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //signInGoogle zamienione na goToHomepage
    private fun goToHomePage(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signIn() {
        val email = sign_in_text_input_email.editText.toString()
        val password = sign_in_text_input_password.editText.toString()
        CoroutineScope(IO).launch {
            val result = UserRepository.loginUserLocal(email, password)
            if (result.success)
                withContext(Main) {
                    goToMain()
                }
            else {
                Log.i("SOMETHING", result.message)
                withContext(Main) {
                    sign_in_text_input_password.error = result.message
                }
            }
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val idToken = account.idToken.toString()
                    CoroutineScope(IO).launch {
                        val result = UserRepository.googleAuth(idToken)
                        if (result.success)
                            withContext(Main) {
                                goToMain()
                            }
                        else {
                            Log.i("SOMETHING", result.message)
                            withContext(Main) {
                                sign_in_text_input_password.error = result.message
                            }
                        }
                    }
                } else
                    Log.i("something", "null account")
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("Error", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun signUp(){
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
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
        val forgotBtn: View = findViewById(R.id.sign_in_dont_remember_password_btn)
        val param = forgotBtn.layoutParams as ConstraintLayout.LayoutParams
        param.setMargins(0,0,0, getSoftButtonsBarSizePort(this))
        forgotBtn.layoutParams = param
    }
}