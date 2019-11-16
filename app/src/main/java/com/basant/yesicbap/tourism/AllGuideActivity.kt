package com.basant.yesicbap.tourism

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toolbar

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import com.basant.yesicbap.tourism.R.layout.activity_all_guide

class AllGuideActivity : AppCompatActivity() {

    private var mFloatingActionButton: FloatingActionButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_all_guide)


        //refrencing
        mFloatingActionButton = findViewById(R.id.all_guide_floating_action_button)


        //when floating action button is clicked
        mFloatingActionButton!!.setOnClickListener {
            val intent = Intent(this@AllGuideActivity, GuideActivity::class.java)
            startActivity(intent)
        }


    } // end on create


}// end of AllGuide activity class
