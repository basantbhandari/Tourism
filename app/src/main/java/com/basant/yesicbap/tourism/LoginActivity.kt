package com.basant.yesicbap.tourism

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var btnSignUp: Button? = null
    private var btnLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        // getting firebase instance
        mAuth = FirebaseAuth.getInstance()


        //for getting all edit text information
        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        btnSignUp = findViewById(R.id.sign_up_button)
        btnLogin = findViewById(R.id.sign_in_button)


        btnLogin!!.setOnClickListener {
            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()

            try {

                if (password.length > 0 && email.length > 0) {
                    mAuth!!.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this@LoginActivity) { task ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(
                                            this@LoginActivity,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show()
                                    Log.v("error", task.result!!.toString())
                                } else {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                } else {
                    Toast.makeText(
                            this@LoginActivity,
                            "Fill All Fields",
                            Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnSignUp!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.forget_password_button).setOnClickListener { startActivity(Intent(applicationContext, ForgetAndChangePassword::class.java).putExtra("Mode", 0)) }

    }  // end of on create


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth!!.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}


