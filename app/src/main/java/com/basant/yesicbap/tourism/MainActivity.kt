package com.basant.yesicbap.tourism

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.Manifest
import android.content.Context
import android.content.Intent

import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.basant.yesicbap.tourism.Constants.ERROR_DIALOG_REQUEST
import com.basant.yesicbap.tourism.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.basant.yesicbap.tourism.Constants.PERMISSIONS_REQUEST_ENABLE_GPS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.main_blog_post_item_card_view.view.*


class MainActivity : AppCompatActivity() {


    //views variable
    private var mToolbar: Toolbar? = null
    //variable for permission GPS
    private var mFloatingActionButton: FloatingActionButton? = null
    private var mMainRecyclerView: RecyclerView? = null

    private var mLocationPermissionGranted = false
    private var mCheck: Boolean = false
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    // for firebase auth
    internal lateinit var auth: FirebaseAuth
    internal var user: FirebaseUser? = null
    lateinit var alertDialog : android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //calling  method
        reference()
        toolBarProperties()



        alertDialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build()






        //fetching user from database
        fetchUser()























        //for checking map services
        mCheck = checkMapServices()
        if (mCheck) {
            //  Toast.makeText(MainActivity.this, "Permission accepted :) ", Toast.LENGTH_LONG).show();


        } else {
            // Toast.makeText(MainActivity.this, "Permission denied :) ", Toast.LENGTH_LONG).show();


        }


        mFloatingActionButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, BlogPostActivity::class.java)
            startActivity(intent)
        }


    }// end of onCreate method



    // fetch user method start



    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/UserPostInformation")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{


            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{

                     Log.d("MainActivity1", " =USER POST INFORMATION" + it.toString())
                    val userPostinformation  = it.getValue(UserPost::class.java)

                    if (userPostinformation != null){
                        adapter.add(UserPostItem(userPostinformation))
                    }

                    //if the item of recycler view is clicked
                    adapter.setOnItemClickListener{ item, view ->
                        val userMe = item as UserPostItem
                        val intent = Intent(view.context, MainItemClickActivity::class.java)
                        intent.putExtra("title", userMe.user.titleBlog)
                        intent.putExtra("image", userMe.user.postImageUrl)
                        intent.putExtra("description", userMe.user.description)
                        startActivity(intent)
                    }





                }

                mMainRecyclerView?.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }




        })



    }
    //fetch user method end

    //start  isMapsEnabled method
    private val isMapsEnabled: Boolean
        get() {
            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
                return false
            }
            return true
        }
    //end isMapsEnabled method

    //start isServiceOk method
    private val isServiceOk: Boolean
        get() {
            Log.d("name", "isServicesOK: checking google services version")

            val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MainActivity)

            if (available == ConnectionResult.SUCCESS) {
                Log.d("me", "isServicesOK: Google Play Services is working")
                return true
            } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
                Log.d("i", "isServicesOK: an error occured but we can fix it")
                val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this@MainActivity, available, ERROR_DIALOG_REQUEST)
                dialog.show()
            } else {
                Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show()
            }
            return false
        }

    // Start of method permission

    //start checkMapService method
    private fun checkMapServices(): Boolean {
        if (isServiceOk) {
            if (isMapsEnabled) {
                return true
            }
        }
        return false
    }
    //end isServiceOk method


    //start buildAlertMessageNoGps method
    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("This application requires access to your mobile location , do you want to enable it ?")
                .setCancelable(false)
                .setPositiveButton("YES") { dialog, id ->
                    val enableGpsIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
                }
        val alert = builder.create()
        alert.show()
    } //end buildAlertMessageNoGps method


    //start getLocationPermission method
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            getLastKnownLocation()
            //location is true then goto where ever you want


        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
    // end method


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {

        mLocationPermissionGranted = false
        when (requestCode) {

            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                //if request is cancelled, the result array are empty
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    getLastKnownLocation()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {

                    //go where ever you want
                    getLastKnownLocation()

                } else {
                    getLocationPermission()

                }
            }
        }
    }


    // end of method permission


    //start toolBarProperties
    private fun toolBarProperties() {
        mToolbar!!.title = "Bhaktapur"
        mToolbar!!.titleMarginStart = 400
        //for toolbar,setting things
        setSupportActionBar(mToolbar)

    }
    //end toolBarProperties

    //start reference
    private fun reference() {
        mToolbar = findViewById(R.id.main_toolbar)
        mFloatingActionButton = findViewById(R.id.main_floating_action_button)
        mMainRecyclerView = findViewById(R.id.main_recycler_view)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser



        getLastKnownLocation()
    }

    private fun getLastKnownLocation(){
        Log.d("we", "getLastKnownLocation: Called")

        // for checking permission
        if (ActivityCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mFusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                val geoPoint = GeoPoint(location!!.latitude, location.longitude)
                 Log.d("name", "onComplete: latitude =" + geoPoint.latitude)
                 Log.d("name", "onComplete: longitudinal =" + geoPoint.longitude)


            }
        }
    }
    //end reference


    //start menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.guide -> {
                //when guide item is clicked
                Log.d("name", "onOptionsItemSelected: I am going to guide activity :)")
                val intentGuide = Intent(this@MainActivity, AllGuideActivity::class.java)
                startActivity(intentGuide)
                return true
            }


            R.id.dash_board -> {
                //when guide item is clicked

                if (auth.currentUser != null) {
                    Log.d("name", "onOptionsItemSelected: I am going to guide activity :)")
                    val intentDashBoard = Intent(this@MainActivity, StartActivity::class.java)
                    startActivity(intentDashBoard)
                } else {
                    Log.d("name", "onOptionsItemSelected: else part")
                    Toast.makeText(this@MainActivity, "You are not currently logged in !!!", Toast.LENGTH_LONG).show()
                }
                super.onResume()

                return true
            }


            R.id.log_Out -> {
                //when guide item is clicked
                Log.d("name", "onOptionsItemSelected: I am going to guide activity :)")
                //to sign out the user user
                FirebaseAuth.getInstance().signOut()
                val signOutIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(signOutIntent)
                finish()

                return true
            }

            R.id.google_map -> {
                //when guide item is clicked
                Log.d("name", "onOptionsItemSelected: I am going to see the map :)")
                val intentToGoogleMap = Intent(this@MainActivity, GoogleMapActivity::class.java)
                startActivity(intentToGoogleMap)

                return true
            }

            R.id.privacy_policy ->
                //when privacy policy item is clicked

                return true

            R.id.help ->
                //when help item is clicked

                return true
            R.id.share -> {
                //when share item is clicked
                shareTourismWithFriends()

                return true
            }
            R.id.exit -> {
                //when exit item is clicked
                showDialogBox()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //end menu


    //start showDialogBox method
    private fun showDialogBox() {
        val alert = android.app.AlertDialog.Builder(this@MainActivity)
        val mView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val button_no = mView.findViewById<Button>(R.id.custom_dialog_no)
        val button_yes = mView.findViewById<Button>(R.id.custom_dialog_yes)
        alert.setView(mView)
        val alertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(false)

        //if no button is clicked
        button_no.setOnClickListener { alertDialog.dismiss() }

        //if yes button is clicked

        button_yes.setOnClickListener {
            //function call to exit from the app
            clickExit()
        }

        //to show alert dialog
        alertDialog.show()

    }

    private fun clickExit() {
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }


    //end showDialogBox method

    //start shareTourismWithFriends method
    private fun shareTourismWithFriends() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareBody = "your body"
        val shareSubject = "your subject"

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
        startActivity(Intent.createChooser(shareIntent, "Share through"))


    }

    //end shareTourismWithFriends method


}



//creating class
class UserPostItem(val user : UserPost ): Item<ViewHolder>(){


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.card_text_view.text = user.titleBlog
        Picasso.get().load(user.postImageUrl).into(viewHolder.itemView.card_image_view)
    }
    override fun getLayout(): Int {
        return R.layout.main_blog_post_item_card_view
    }

}