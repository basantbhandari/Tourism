package com.basant.yesicbap.tourism

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {


    internal lateinit var btnSignOut: Button
    internal lateinit var auth: FirebaseAuth
    internal var user: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        auth = FirebaseAuth.getInstance()
        user = auth.currentUser



        btnSignOut = findViewById(R.id.sign_out_button)

        btnSignOut.setOnClickListener {
            Log.d("name", "onClick:I am going to login activity  ")
            //to sign out the user user
            FirebaseAuth.getInstance().signOut()
            val signOutIntent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(signOutIntent)
            finish()
        }

        findViewById<View>(R.id.change_password_button).setOnClickListener { startActivity(Intent(applicationContext, ForgetAndChangePassword::class.java).putExtra("Mode", 1)) }

        findViewById<View>(R.id.change_email_button).setOnClickListener { startActivity(Intent(applicationContext, ForgetAndChangePassword::class.java).putExtra("Mode", 2)) }

        findViewById<View>(R.id.delete_user_button).setOnClickListener { startActivity(Intent(applicationContext, ForgetAndChangePassword::class.java).putExtra("Mode", 3)) }

    }// end create


}
