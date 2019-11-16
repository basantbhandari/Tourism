package com.basant.yesicbap.tourism

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog

class RegisterActivity : AppCompatActivity() {

    private var mInputEmail: EditText? = null
    private var mInputPassword: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var mSignUp: Button? = null
    private var mLogin: Button? = null
    private var mForgetPassword: Button? = null


    // for progress bar
    lateinit var alertDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        alertDialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build()




        //getting firebase instance
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser != null) {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }


        //refrencing view
        mInputEmail = findViewById(R.id.register_email)
        mInputPassword = findViewById(R.id.register_password)
        mSignUp = findViewById(R.id.register_sign_up_button)
        mLogin = findViewById(R.id.register_sign_in_button)
        mForgetPassword = findViewById(R.id.register_forget_password)



        mSignUp!!.setOnClickListener {
            val email = mInputEmail!!.text.toString()
            val password = mInputPassword!!.text.toString()

            try {
                if (password.length > 0 && email.length > 0) {
                    alertDialog.show()
                    mAuth!!.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this@RegisterActivity) { task ->
                                if (!task.isSuccessful) {
                                    alertDialog.dismiss()
                                    Toast.makeText(
                                            this@RegisterActivity,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show()
                                    Log.v("error", task.result!!.toString())
                                } else {
                                    alertDialog.dismiss()
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                } else {
                    Toast.makeText(
                            this@RegisterActivity,
                            "Fill All Fields",
                            Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mLogin!!.setOnClickListener {
            val ijk = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(ijk)
            finish()
        }



        mForgetPassword!!.setOnClickListener {
            val intentForget = Intent(this@RegisterActivity, ForgetAndChangePassword::class.java)
            intentForget.putExtra("Mode", 0)
            startActivity(intentForget)
        }

    }// ending of on create
}
