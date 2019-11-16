package com.basant.yesicbap.tourism

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


import com.google.firebase.auth.FirebaseAuth

class ForgetAndChangePassword : AppCompatActivity() {


    private var mTitle: TextView? = null
    private var mMainTitle: TextView? = null
    private var mEmail: EditText? = null
    private var mSubmit: Button? = null

    private var auth: FirebaseAuth? = null

    private var mode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_and_change_password)


        //getting firebase instance
        auth = FirebaseAuth.getInstance()

        //referencing
        mTitle = findViewById(R.id.forget_title)
        mEmail = findViewById(R.id.forget_email)
        mSubmit = findViewById(R.id.forget_submit_button)
        mMainTitle = findViewById(R.id.forget_main_title)

        //receiving intent
        val i = intent
        mode = i.getIntExtra("Mode", 0)


        if (mode == 0) {
            mMainTitle!!.text = "Forget Password"
            mEmail!!.hint = "Enter Registered Email"
            mTitle!!.text = "Enter Registered Email"
        } else if (mode == 1) {
            mMainTitle!!.text = "Change Password"
            mEmail!!.hint = "Enter New Password"
            mTitle!!.text = "Enter New Password"
        } else if (mode == 2) {
            mMainTitle!!.text = "Change Email"
            mEmail!!.hint = "Enter New Email"
            mTitle!!.text = "Enter New Email"
        } else {

            mMainTitle!!.text = "Delete User"
            mTitle!!.text = "Are you sure ."
            mEmail!!.visibility = View.GONE
        }

        mSubmit!!.setOnClickListener { callFunction(mode) }
    } // end create

    private fun callFunction(mode: Int) {

        val user = auth!!.currentUser
        val modeStr = mEmail!!.text.toString()
        if (mode == 0) {
            if (TextUtils.isEmpty(modeStr)) {
                mEmail!!.error = "Value Required"
            } else {
                auth!!.sendPasswordResetEmail(modeStr).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ForgetAndChangePassword, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                        val intentGo = Intent(this@ForgetAndChangePassword, MainActivity::class.java)
                        startActivity(intentGo)


                    } else {
                        Toast.makeText(this@ForgetAndChangePassword, "Failed to send reset email! Please try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (mode == 1) {
            if (TextUtils.isEmpty(modeStr)) {
                mEmail!!.error = "Value Required"
            } else {
                user!!.updatePassword(modeStr)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ForgetAndChangePassword, "Password is updated!", Toast.LENGTH_SHORT).show()
                                val intentGoo = Intent(this@ForgetAndChangePassword, MainActivity::class.java)
                                startActivity(intentGoo)

                            } else {
                                Toast.makeText(this@ForgetAndChangePassword, "Failed to update password. ! Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        } else if (mode == 2) {
            if (TextUtils.isEmpty(modeStr)) {
                mEmail!!.error = "Value Required"
            } else {
                user!!.updateEmail(modeStr)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ForgetAndChangePassword, "Email address is updated.", Toast.LENGTH_LONG).show()
                                val intentGooo = Intent(this@ForgetAndChangePassword, MainActivity::class.java)
                                startActivity(intentGooo)

                            } else {
                                Toast.makeText(this@ForgetAndChangePassword, "Failed to update email! Please try again.", Toast.LENGTH_LONG).show()
                            }
                        }
            }
        } else {

            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ForgetAndChangePassword, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show()
                    val intentGoooo = Intent(this@ForgetAndChangePassword, RegisterActivity::class.java)
                    startActivity(intentGoooo)

                } else {
                    Toast.makeText(this@ForgetAndChangePassword, "Failed to delete your account! Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }// call end function


}// end main class
