package com.twoSecure.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.Towrevo.application.MyApp.Companion.myCurrentLocation

object LocationUtils {

    fun setUpLocation(
        activity: Activity,
        onLocationReceived: (Location) -> Unit = {},
        onLocationUpdate: (Location) -> Unit = {}
    ) {

        myCurrentLocation = null

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            if (myCurrentLocation == null) {
                                myCurrentLocation = location
                                onLocationReceived(location)
                            } else {
                                onLocationUpdate(location)
                                myCurrentLocation = location
                            }
                        }
                    }
                },
                Looper.myLooper()
            )

        }


    }

    fun updateCamera(googleMap: GoogleMap, latitude:Double , longitude:Double,zoomLevel : Float = 12f) {
        val latLng = LatLng(latitude, longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
        googleMap.moveCamera(cameraUpdate)
    }

    fun updateCamera(googleMap: GoogleMap,latLng: LatLng,zoomLevel : Float = 16f) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
        googleMap.moveCamera(cameraUpdate)
    }
}