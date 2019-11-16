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
import com.google.firebase.database.FirebaseDatabase


import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog

import java.io.File
import java.util.*




class BlogPostActivity : AppCompatActivity() {

    //view

    private  var TAG = "BlogPostActivity"

    private var mImageView: ImageView? = null
    private var mEditText: EditText? = null
    private var mEditTextTitle: EditText? = null

    private var mButton: Button? = null
    //var
    private var blogImageUri: Uri? = null

    private var description: String? = null
    private var titleBlog: String? = null

    lateinit var alertDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_post)

        //refrencing

        mImageView = findViewById(R.id.blog_post_image)
        mEditText = findViewById(R.id.blog_post_description)
        mEditTextTitle = findViewById(R.id.blog_post_title)
        mButton = findViewById(R.id.blog_post_submit)


        alertDialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build()





        mImageView!!.setOnClickListener {
            ImagePicker.with(this)
                    .crop()	    			                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)		            	//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
        }





        mButton!!.setOnClickListener {
            Log.d("name", "onClick: submit button is clicked")
            titleBlog = mEditTextTitle!!.text.toString()
            description = mEditText!!.text.toString()
            if (!TextUtils.isEmpty(description) && blogImageUri != null && !TextUtils.isEmpty(titleBlog)) {

                Log.d("name", "onClick:fields are properly field")
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
                                       //Link =  it.toString()
                                        Log.d(TAG, "DownloadUrl =  $it")
                                    }
                            saveAllDataToFirebaseDatabase(it.toString())

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

    private fun saveAllDataToFirebaseDatabase(profileImageUrl : String) {


        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/UserPostInformation/$uid")

        val userpost  = uid?.let { titleBlog?.let { it1 -> description?.let { it2 -> UserPost(it, it1, it2, profileImageUrl ) } } }

        ref.setValue(userpost)
                .addOnSuccessListener {
                    Log.d(TAG, "Data are also inserted into firebase database")
                }
                .addOnFailureListener{
                    Log.d(TAG, "Failed to upload the data")
                }



    }


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



//class for uploading data to the firebase database
class UserPost(val uid: String, val titleBlog : String , val description : String, val postImageUrl : String )