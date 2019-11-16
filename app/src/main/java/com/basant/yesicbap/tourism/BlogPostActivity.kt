package com.basant.yesicbap.tourism

import androidx.appcompat.app.AppCompatActivity


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import dmax.dialog.SpotsDialog

import java.io.File
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class BlogPostActivity : AppCompatActivity() {

    //view

    private  var TAG = "BlogPostActivity"

    private var mImageView: ImageView? = null
    private var mEditText: EditText? = null
    private var mButton: Button? = null
    //var
    private var blogImageUri: Uri? = null
    private var Link: String = ""
    //firebase refrence
    lateinit var db : FirebaseFirestore
    private var mFirebaseAuth: FirebaseAuth? = null

    private var current_user_id: String? = null
    private var description: String? = null
    lateinit var alertDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_post)

        //refrencing

        mImageView = findViewById(R.id.blog_post_image)
        mEditText = findViewById(R.id.blog_post_description)
        mButton = findViewById(R.id.blog_post_submit)

     //    db = FirebaseFirestore.getInstance()        //initializing database
        alertDialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build()

       // current_user_id = Objects.requireNonNull<FirebaseUser>(mFirebaseAuth!!.currentUser).getUid()




        mImageView!!.setOnClickListener {
            ImagePicker.with(this)
                    .crop()	    			                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)		            	//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
        }





        mButton!!.setOnClickListener {
            Log.d("name", "onClick: submit button is clicked")
            description = mEditText!!.text.toString()
            if (!TextUtils.isEmpty(description) && blogImageUri != null) {

                Log.d("name", "onClick: submit button is clicked again")


                alertDialog.show()


                val fileName = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("/PostImage/$fileName")

                ref.putFile(blogImageUri!!)
                        .addOnSuccessListener {
                            alertDialog.dismiss()
                            Log.d(TAG, "Successfully inserted image.${it.metadata?.path}")

                           //for download uri
                            ref.downloadUrl
                                    .addOnSuccessListener {
                                       Link =  it.toString()

                                        Log.d(TAG, "DownloadUrl = " + Link.toString())
                                    }
/*

                            val post = hashMapOf(
                                    "description" to description,
                                    "imageUri" to Link
                            )

                            db.collection("AllPost").document("post")
                                    .set(post as Map<String, Any>)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "DocumentSnapshot successfully written!")
                                    }
                                    .addOnFailureListener {
                                        Log.d(TAG, "Error writing document")
                              }
                            */



                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()



                        }.addOnFailureListener{
                            alertDialog.dismiss()
                            Toast.makeText(this, "Image is not inserted", Toast.LENGTH_LONG).show()
                        }

















            } else {


                Toast.makeText(applicationContext, "please fulfill the fields", Toast.LENGTH_LONG).show()

            }
        }


    }// end create



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            blogImageUri = data?.data
            mImageView?.setImageURI(blogImageUri)

            //You can get File object from intent
            val file: File? = ImagePicker.getFile(data)

            //You can also get File Path from intent
            val filePath: String? = ImagePicker.getFilePath(data)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


}
