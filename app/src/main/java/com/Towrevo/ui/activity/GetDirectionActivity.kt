package com.Towrevo.ui.activity

import android.Manifest
import android.graphics.Color
import android.location.Location
import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.*
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.ui.datamodel.CustomerDetailsModel
import com.Towrevo.ui.datamodel.NearByCompanyModel
import com.Towrevo.util.COMPANAY_DETAIL_CONTAINER
import com.Towrevo.util.CUSTOMER_DETAIL_CONTAINER
import com.Towrevo.util.PermissionUtils
import com.Towrevo.util.Util
import com.twoSecure.util.LocationUtils
import kotlinx.android.synthetic.main.activity_get_direction.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class GetDirectionActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnMapClickListener, LocationListener {

    private var googleMap: GoogleMap? = null
    private lateinit var currentLat: String
    private lateinit var currentLong: String
    private var isMapLoaded = false
    private var lat = 0.0
    private var long = 0.0
    private lateinit var companyDetail: NearByCompanyModel.Data
    private lateinit var customerDetail: CustomerDetailsModel.Data
    private lateinit var marker: Marker
    private var iscompanyDetail = ""



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        setContentView(R.layout.activity_get_direction)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //
//        window.decorView.systemUiVisibility =
//            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//               or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

        setLabel()
        setData()
        setUpMap()

        if (intent.hasExtra(COMPANAY_DETAIL_CONTAINER)) {
            companyDetail =
                intent.getSerializableExtra(COMPANAY_DETAIL_CONTAINER) as NearByCompanyModel.Data
            iscompanyDetail="1"
        }
        if (intent.hasExtra(CUSTOMER_DETAIL_CONTAINER)) {
            customerDetail =
                intent.getSerializableExtra(CUSTOMER_DETAIL_CONTAINER) as CustomerDetailsModel.Data
            iscompanyDetail="0"
        }
    }

    private fun setData() {
        backiv.setOnClickListener(this)

    }

    private fun setLabel() {

    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backiv -> onBackPressed()

        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap!!.setOnMapLoadedCallback(this)
        googleMap!!.setOnMapClickListener(this)
        PermissionUtils.checkForPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            myCallBack = {
                if (it) {
                    googleMap!!.isMyLocationEnabled = true
                    LocationUtils.setUpLocation(this, onLocationReceived = { location ->
                        LocationUtils.updateCamera(
                            googleMap!!,
                            location.latitude,
                            location.longitude
                        )
                    })
                }
            })

    }

    override fun onMapLoaded() {

        LocationUtils.setUpLocation(this, onLocationReceived = { location ->
            LocationUtils.updateCamera(googleMap!!, location.latitude, location.longitude)

            currentLat = location.latitude.toString()
            currentLong = location.longitude.toString()
            isMapLoaded = true
            // callUpdateCurrentLocationApi()

            /* val markerOptions = MarkerOptions()
             markerOptions.position(LatLng(currentLat.toDouble(), currentLong.toDouble()))
             markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))

             marker = googleMap!!.addMarker(markerOptions)
             googleMap!!.addMarker(markerOptions)*/


            if (iscompanyDetail.equals("1")){
                drawRoute(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), LatLng(
                        companyDetail.place.latitude.toDouble(),
                        companyDetail.place.longitude.toDouble()
                    )
                )

            }else if (iscompanyDetail.equals("0")){
                drawRoute(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), LatLng(
                        customerDetail.place.latitude.toDouble(),
                        customerDetail.place.longitude.toDouble()
                    )
                )

            }


            Log.e(">>current", ">>" + "${currentLong}: ${currentLat}")
        })


    }


    private fun drawRoute(source: LatLng, destination: LatLng) {

        val source_marker: BitmapDescriptor =
            BitmapDescriptorFactory.fromResource(R.drawable.pin1)

/*
            getMarkerIconFromDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_blue_dot
                ) as Drawable
            ) as BitmapDescriptor
*/
        val destination_marker: BitmapDescriptor =
            BitmapDescriptorFactory.fromResource(R.drawable.pin2)


        googleMap!!.addMarker(
            MarkerOptions().icon(source_marker).position(source).title("Your Location")
        )
        googleMap!!.addMarker(
            MarkerOptions().icon(destination_marker).position(destination).title("Compamy Location")
        )

        val options = PolylineOptions()
        options.color(Color.BLACK)
        options.width(8f)
        val LatLongB = LatLngBounds.Builder()

        val url = Util.getURL(source, destination)
        Log.e("URL", url)


        /*val polyline1 = googleMap!!.addPolyline(PolylineOptions()
            .clickable(true)
            .add(
                currentLocation,
                destinationLocation))*/

        doAsync {
            // Connect to URL, download content and convert into string asynchronously
            val result = URL(url).readText()
            uiThread {
                // When API call is done, create parser and convert into JsonObjec
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")
                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                val polypts =
                    points.map { Util.decodePoly(it.obj("polyline")?.string("points")!!) }
                // Add  points to polyline and bounds
                options.add(source)
                LatLongB.include(source)
                for (point in polypts) {
                    options.addAll(point)
                    for (i in point.indices) {
                        LatLongB.include(point[i])
                    }
//                    LatLongB.include(point)
                }
                options.add(destination)
                LatLongB.include(destination)
                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                googleMap!!.addPolyline(options)
                // show map with route centered
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }
        // showEstimationBottomSheet()
    }


    override fun onMapClick(p0: LatLng?) {
    }

    override fun onLocationChanged(p0: Location?) {
        MyApp.myCurrentLocation = p0
        currentLat = MyApp.myCurrentLocation?.latitude.toString()
        currentLong = MyApp.myCurrentLocation?.longitude.toString()
    }
}