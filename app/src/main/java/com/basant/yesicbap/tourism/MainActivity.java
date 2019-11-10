package com.basant.yesicbap.tourism;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;


import static com.basant.yesicbap.tourism.Constants.ERROR_DIALOG_REQUEST;
import static com.basant.yesicbap.tourism.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.basant.yesicbap.tourism.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;


public class MainActivity extends AppCompatActivity  {


    //views variable
    private Toolbar mToolbar;
    //variable for permission GPS
    private FloatingActionButton mFloatingActionButton;
    private boolean mLocationPermissionGranted = false;
    private boolean mCheck;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // for firebase auth
    FirebaseAuth auth;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calling  method
        reference();
        toolBarProperties();

       //for checking map services
        mCheck = checkMapServices();

        if (mCheck){
          //  Toast.makeText(MainActivity.this, "Permission accepted :) ", Toast.LENGTH_LONG).show();




        }else {
           // Toast.makeText(MainActivity.this, "Permission denied :) ", Toast.LENGTH_LONG).show();





        }


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlogPostActivity.class);
                startActivity(intent);
            }
        });



    }// end of onCreate method




    // Start of method permission

    //start  checkMapService method
    private boolean checkMapServices() {
        if (isServiceOk()){
            if (isMapsEnabled()){
                return true;
            }
        }
        return false;
    }


    //end checkMapService method

    //start  isMapsEnabled method
    private boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }
    //end isMapsEnabled method

    //start isServiceOk method
    private boolean isServiceOk() {
        Log.d("name", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d("me", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d("i", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    //end isServiceOk method





    //start buildAlertMessageNoGps method
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires access to your mobile location , do you want to enable it ?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    } //end buildAlertMessageNoGps method



    //start getLocationPermission method
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                 getLastKnownLocation();
              //location is true then goto where ever you want


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    // end method




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode){

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                //if request is cancelled, the result array are empty
                if (grantResults.length > 0
                  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                    getLastKnownLocation();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PERMISSIONS_REQUEST_ENABLE_GPS:{
                if (mLocationPermissionGranted){

                    //go where ever you want
                     getLastKnownLocation();

                }else {
                    getLocationPermission();

                }
            }
        }
    }


    // end of method permission




        //start toolBarProperties
        private void toolBarProperties () {
            mToolbar.setTitle("Bhaktapur");
            mToolbar.setTitleMarginStart(400);
            //for toolbar,setting things
            setSupportActionBar(mToolbar);

        }
        //end toolBarProperties

        //start reference
        private void reference () {
            mToolbar = findViewById(R.id.main_toolbar);
            mFloatingActionButton = findViewById(R.id.main_floating_action_button);
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();



            getLastKnownLocation();
        }

    private void getLastKnownLocation() {
        Log.d("we", "getLastKnownLocation: Called");

        // for checking permission
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()){
                            Location location = task.getResult();
                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            Log.d("name", "onComplete: latitude =" + geoPoint.getLatitude());
                            Log.d("name", "onComplete: longitudinal =" + geoPoint.getLongitude());


                        }
                    }
                }
        );
    }
    //end reference


        //start menu
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.guide:
                    //when guide item is clicked
                    Log.d("name", "onOptionsItemSelected: I am going to guide activity :)");
                  //  Intent intentGuide = new Intent(MainActivity.this, AllGuideActivity.class);
                   // startActivity(intentGuide);
                    return true;


                case R.id.dash_board:
                    //when guide item is clicked

                    if (auth.getCurrentUser() != null) {
                        Log.d("name", "onOptionsItemSelected: I am going to guide activity :)");
                        Intent intentDashBoard = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(intentDashBoard);
                    }else {
                        Log.d("name", "onOptionsItemSelected: else part");
                        Toast.makeText(MainActivity.this,"You are not currently logged in !!!",Toast.LENGTH_LONG).show();
                    }
                    super.onResume();

                    return true;



                case R.id.log_Out:
                    //when guide item is clicked
                    Log.d("name", "onOptionsItemSelected: I am going to guide activity :)");
                    //to sign out the user user
                    FirebaseAuth.getInstance().signOut();
                    Intent signOutIntent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(signOutIntent);
                    finish();

                    return true;

                case R.id.google_map:
                    //when guide item is clicked
                    Log.d("name", "onOptionsItemSelected: I am going to see the map :)");
                    Intent intentToGoogleMap = new Intent(MainActivity.this, GoogleMapActivity.class);
                    startActivity(intentToGoogleMap);

                    return true;

                case R.id.privacy_policy:
                    //when privacy policy item is clicked

                    return true;

                case R.id.help:
                    //when help item is clicked

                    return true;
                case R.id.share:
                    //when share item is clicked
                    shareTourismWithFriends();

                    return true;
                case R.id.exit:
                    //when exit item is clicked
                    showDialogBox();

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    //end menu


    //start showDialogBox method
    private void showDialogBox() {
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);
        final Button button_no = mView.findViewById(R.id.custom_dialog_no);
        final Button button_yes = mView.findViewById(R.id.custom_dialog_yes);
        alert.setView(mView);
        final android.app.AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //if no button is clicked
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        //if yes button is clicked

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function call to exit from the app
                clickExit();
            }
        });

        //to show alert dialog
        alertDialog.show();

    }

    private void clickExit() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    //end showDialogBox method

    //start shareTourismWithFriends method
    private void shareTourismWithFriends() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "your body";
        String shareSubject = "your subject";

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        startActivity(Intent.createChooser(shareIntent,"Share through"));


    }

    //end shareTourismWithFriends method


}
