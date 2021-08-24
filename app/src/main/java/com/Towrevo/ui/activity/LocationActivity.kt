package com.Towrevo.ui.activity

import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.Towrevo.R
import com.Towrevo.util.PermissionUtils
import com.twoSecure.util.LocationUtils.setUpLocation
import com.twoSecure.util.LocationUtils.updateCamera
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity(), View.OnClickListener , OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
    LocationListener {
    private var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_location)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setUpMap()
        setData()
    }
    private fun setData(){
        backArrowIV.setOnClickListener(this)
        listViewIV.setOnClickListener(this)
    }
    private fun setUpMap(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.backArrowIV -> finish()

        }
    }

    override fun onLocationChanged(location: Location?) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap!!.setOnMapLoadedCallback(this)
        PermissionUtils.checkForPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            myCallBack = {
                if (it) {
                    googleMap!!.isMyLocationEnabled = true

                }
            })
    }

    override fun onMapLoaded() {
        setUpLocation(this, onLocationReceived = { location ->
            updateCamera(googleMap!!, location.latitude, location.longitude)
        })
    }

}