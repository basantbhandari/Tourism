package com.basant.yesicbap.tourism


import androidx.appcompat.app.AppCompatActivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.io.File
import java.util.*







class GuideActivity : AppCompatActivity() {

    private var mFirstName: EditText? = null
    private var mLastName: EditText? = null
    private var mImageView: ImageView? = null
    private var mPhoneNumber: EditText? = null
    private var mAge: EditText? = null
    private var mDescription: EditText? = null
    private var mSubmit: Button? = null
    private var checked: Boolean? = null


    private var GuideImageURI: Uri? = null
    private var status: Boolean? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var phoneNumber: String? = null
    private var age: Int? = null
    private var description: String? = null

    // for progress bar
    lateinit var alertDialog : AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        //referencing
        mFirstName = findViewById(R.id.guide_guide_first_name)
        mLastName = findViewById(R.id.guide_guide_last_name)
        mImageView = findViewById(R.id.guide_image)
        mPhoneNumber = findViewById(R.id.guide_guide_phone_number)
        mAge = findViewById(R.id.guide_guide_age)
        mDescription = findViewById(R.id.guide_guide_describtion)
        mSubmit = findViewById(R.id.guide_submit_button)


        alertDialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build()




        // when image is opened
        mImageView!!.setOnClickListener {
          // for picking image from library

            ImagePicker.with(this)
                    .crop()	    			                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)		            	//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()

        }





        mSubmit!!.setOnClickListener {
            //perform some task
            sendGuideInformationToDatabase()
        }


    }// end create



    private fun sendGuideInformationToDatabase() {

     if (mFirstName!!.text.toString().trim { it <= ' ' }.isEmpty() || mLastName!!.text.toString().trim { it <= ' ' }.isEmpty() || GuideImageURI == null
                || mPhoneNumber!!.text.toString().trim { it <= ' ' }.isEmpty() ||
                mAge!!.text.toString().trim { it <= ' ' }.isEmpty() || mDescription!!.text.toString().trim { it <= ' ' }.isEmpty() || !checked!!) {


            Toast.makeText(this@GuideActivity, "Field are Empty.", Toast.LENGTH_LONG).show()
            Log.d(TAG, "sendGuideInformationToDatabase: filed are empty")


        } else {

            firstName = mFirstName!!.text.toString()
            lastName = mLastName!!.text.toString()
            phoneNumber = mPhoneNumber!!.text.toString()
            if (mAge!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(applicationContext, "DATA FIELDS ARE NOT FILLED PROPERLY !!!", Toast.LENGTH_LONG).show()
            } else {
                age = Integer.valueOf(mAge!!.text.toString())
            }
            description = mDescription!!.text.toString()
            // there is status as boolean value
         alertDialog.show()

         val fileName = UUID.randomUUID().toString()
         val ref = FirebaseStorage.getInstance().getReference("/Images/$fileName")
         Log.d(TAG, "Image is uploading...")
         ref.putFile(GuideImageURI!!)
                 .addOnSuccessListener {
                     alertDialog.dismiss()
                     Log.d(TAG, "Successfully inserted image.${it.metadata?.path}")

                     //for download uri
                     ref.downloadUrl
                             .addOnSuccessListener {
                                 Log.d(TAG, "GuideActivity :DownloadUrl :$it")

                                 //method to save data to firebase database
                                 saveUserDataToFirebaseDatabase(it.toString())
                             }


                     var intent = Intent(this, AllGuideActivity::class.java)
                     startActivity(intent)
                     finish()
                 }.addOnFailureListener{
                     alertDialog.dismiss()
                     Toast.makeText(this, "Image is not inserted", Toast.LENGTH_LONG).show()
                 }

        }// end else


    }  // end function



    private fun saveUserDataToFirebaseDatabase(imageUrl: String ) {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/GuideInformation/$uid")

        val gInfo  = GuideInfo(uid, firstName, lastName, imageUrl, phoneNumber, age, description, status)

        Log.d("me", "imageUrl = $imageUrl")
        ref.setValue(gInfo)
                .addOnSuccessListener {
                    Log.d(TAG, " Guide Data are also inserted into firebase database")
                }
                .addOnFailureListener{
                    Log.d(TAG, "Failed to upload the data")
                }
    }


    //start radio button
    fun onRadioButtonClicked(view: View) {

        checked = (view as RadioButton).isChecked

        // Check which radio button was clicked
        when (view.getId()) {
            R.id.guide_guide_male -> if (checked!!) {
                // male is checked
                status = true
            }
            R.id.guide_guide_female -> if (checked!!) {
                // female is checked
                status = false
            }
        }
    }

    //end radio button





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            GuideImageURI = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, GuideImageURI)
            mImageView?.setImageBitmap(bitmap)
            Log.d(TAG, "File selected : " + GuideImageURI.toString())
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

    companion object {
        private val TAG = "GuideActivity"
    }
}

//class for uploading data to the firebase database
class GuideInfo(val uid: String?, val firstName: String?, val lastName: String?, val imageUrl: String, val phoneNumber: String?, val age: Int?, val description: String?, val Status: Boolean?){
    constructor():this("","", "", "", "", 1, "", true)

}