package com.basant.yesicbap.tourism

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.basant.yesicbap.tourism.R.layout.activity_all_guide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.guide_list_item_post_layout.view.*

class AllGuideActivity : AppCompatActivity() {

    private var mAllGuideRecyclerView : RecyclerView ?= null
    private var mFloatingActionButton: FloatingActionButton? = null

    lateinit var alertDialog : AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_all_guide)



        //refrencing
        mFloatingActionButton = this.findViewById(R.id.all_guide_floating_action_button)
        mAllGuideRecyclerView = this.findViewById(R.id.all_guide_recycler_view)




        alertDialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build()



        //fetching Guiding from database

         fetchAllGuide()



        //when floating action button is clicked
        mFloatingActionButton!!.setOnClickListener {
            val intent = Intent(this@AllGuideActivity, GuideActivity::class.java)
            startActivity(intent)
        }






    } // end on create



    private fun fetchAllGuide() {

        val ref = FirebaseDatabase.getInstance().getReference("/GuideInformation")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                  val myAdapterForGuige = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    Log.d("AllGuideActivity", " dataRetrive =" + it.toString())

                    val userGuide = it.getValue(GuideInfo::class.java)
                    if (userGuide != null){

                        myAdapterForGuige.add(UserGuideItem(userGuide))

                    }
                    //if the item of recycler view is clicked
                    myAdapterForGuige.setOnItemClickListener{ item, view ->
                        val userP = item as UserGuideItem
                        Log.d("yo", "status = "+ userP.guide.Status)

                        val intent = Intent(view.context, AllGuideItemClickActivity::class.java)
                        intent.putExtra("name", userP.guide.firstName+" "+userP.guide.lastName)
                        intent.putExtra("image", userP.guide.imageUrl)
                        intent.putExtra("phone", userP.guide.phoneNumber)
                        intent.putExtra("age", userP.guide.age)
                        intent.putExtra("gender", userP.guide.Status)
                        intent.putExtra("description", userP.guide.description)

                        startActivity(intent)

                    }




                }

                mAllGuideRecyclerView?.adapter = myAdapterForGuige

            }
            override fun onCancelled(p0: DatabaseError) {


            }

        } )

    }






}// end of AllGuide activity class





class UserGuideItem(val guide: GuideInfo) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.guide_list_item_post_layout_name.text  = "Name: "+guide.firstName + " "+ guide.lastName
        Picasso.get().load(guide.imageUrl).into(viewHolder.itemView.guide_list_item_post_layout_image_view)
        Log.d("AllGuideActivity", "Successful shown in recycler view ")
    }
    override fun getLayout(): Int {
        return R.layout.guide_list_item_post_layout
    }
}




