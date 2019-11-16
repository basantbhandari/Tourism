package com.basant.yesicbap.tourism

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.basant.yesicbap.tourism.Constants.MAPVIEW_BUNDLE_KEY

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMapView: MapView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)
        //for map view start
        initGoogleMap(savedInstanceState)

        //for map view end
    }// end on create


    //start map method
    private fun initGoogleMap(savedInstanceState: Bundle?) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView = findViewById(R.id.main_map_view)
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this)
    }

    //end map method


    //start implemented OnMapReadyCallback


    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mMapView!!.onSaveInstanceState(mapViewBundle)
    }

    public override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    public override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
    }

    public override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
    }

    override fun onMapReady(map: GoogleMap) {
        map.addMarker(MarkerOptions().position(LatLng(27.671022, 85.429817)).title("Marker"))
        if (ActivityCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        map.isMyLocationEnabled = true
    }

    public override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    public override fun onDestroy() {
        mMapView!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }
    //end implemented OnMapReadyCallback


} // end main class
