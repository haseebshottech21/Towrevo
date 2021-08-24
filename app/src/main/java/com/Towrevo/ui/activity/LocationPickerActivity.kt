package com.Towrevo.ui.activity

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionManager
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.application.MyApp.Companion.myCurrentLocation
import com.Towrevo.labels.CANCEL
import com.Towrevo.labels.LabelUtils
import com.Towrevo.labels.SEARCH_LOCATION
import com.Towrevo.labels.SELECT
import com.Towrevo.util.*
import com.Towrevo.util.extension.*
import com.towrevo.ui.datamodel.CreateTripModel
import com.twoSecure.util.LocationUtils
import kotlinx.android.synthetic.main.activity_location_picker.*
import kotlinx.android.synthetic.main.bottomsheet_location_address.*
import java.io.IOException
import java.util.*

class
LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
    View.OnClickListener {

    private var mMap: GoogleMap? = null
    private var centerLatLng: LatLng? = null
    private var myHandler: Handler? = null
    private var myRunnable: Runnable? = null
    private var autocompleteFragment: AutocompleteSupportFragment? = null
    private val ANIMATED_BOTTOM_LAYOUT_INTERVAL = 1000L
    private val DEFAULT_ZOOM = 20
    private var slideFromBottom: Animation? = null
    private var slideToBottom: Animation? = null
    var placename: String = ""
    private lateinit var place: CreateTripModel.Places
    private var source = ""

    private var sourceLat = ""
    private var sourceLong = ""
    private var fromHome = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setLabel()
        setData()


    }

    private fun setData() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.GOOGLE_MAP_KEY))
        }
        setUpMap()
        if (intent.hasExtra("Home")) {

            fromHome = intent.getStringExtra("Home") as String
        }


        setUpAutoCompletePicker()
        initAnim()
        searchLocationLL.setOnClickListener(this)
        cancelBTN.setOnClickListener(this)
        selectBTN.setOnClickListener(this)
    }

    private fun setLabel() {
        searchLocationTV.hint = LabelUtils.getLabel(this, SEARCH_LOCATION, R.string.search_location)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isTiltGesturesEnabled = true
        mMap?.uiSettings?.isScrollGesturesEnabled = true
        mMap?.uiSettings?.isZoomControlsEnabled = false
        mMap?.setOnCameraMoveStartedListener(this)
        mMap?.setOnCameraIdleListener(this)
        mMap?.setOnMapLoadedCallback(this)
        mMap?.isBuildingsEnabled = true
        mMap?.isIndoorEnabled = true
        mMap?.uiSettings?.isRotateGesturesEnabled = false

    }

    override fun onCameraMoveStarted(reason: Int) {
        llAdd.gone()
        invalidHandler()
    }

    override fun onMapLoaded() {
        invalidHandler()
        LocationUtils.updateCamera(
            mMap!!,
            myCurrentLocation?.latitude ?: 0.0000,
            myCurrentLocation?.longitude ?: 0.0000,
            17f
        )



    }


    override fun onCameraMove() {
        llAdd.gone()
        invalidHandler()
    }

    override fun onCameraIdle() {
        centerLatLng = mMap!!.cameraPosition.target
        initHandler()
    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setUpAutoCompletePicker() {

        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteFragment?.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS
            )
        )

        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                centerLatLng = place.latLng
                searchLocationTV.text = place.name
                placename = place.name.toString()
                mMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        centerLatLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }

            override fun onError(p0: Status) {


            }
        })


    }


    private fun initHandler() {
        invalidHandler()
        myHandler = Handler(Looper.myLooper()!!)
        myRunnable = Runnable {
            pinIV.visible()
            showAddressDialog(getAddress())

        }
        myHandler!!.postDelayed(myRunnable!!, ANIMATED_BOTTOM_LAYOUT_INTERVAL)
    }

    private fun getAddress(): String {
        val result = StringBuilder()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(centerLatLng!!.latitude, centerLatLng!!.longitude, 1)
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                result.append(address.getAddressLine(0))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result.toString()
    }

    private fun invalidHandler() {

        myHandler?.removeCallbacks(myRunnable!!)
        myHandler = null
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.searchLocationLL -> {
                val root: View? = autocompleteFragment?.view
                root?.post {
                    root.findViewById<View>(R.id.places_autocomplete_search_input)
                        .performClick()
                }
            }
            R.id.cancelBTN -> {
                llAdd.gone()
                llAdd.startAnimation(slideToBottom)
            }
        }
    }

    private fun initAnim() {
        slideFromBottom =
            AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom)
        slideToBottom =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)

        slideFromBottom!!.duration = 200
    }


    private fun showAddressDialog(address: String) {
        if (llAdd.isGone()) {
            TransitionManager.beginDelayedTransition(rootRL)
            llAdd.visible()
            llAdd.startAnimation(slideFromBottom)
            // llViewLoc.startAnimation(slideFromBottom);

            addressTV.text = address
            selectBTN.text = LabelUtils.getLabel(this, SELECT, R.string.select)
            cancelBTN.text = LabelUtils.getLabel(this, CANCEL, R.string.cancel)

            selectBTN.setOnClickListener {

                if (fromHome.equals("1")) {
                    MyApp.destinationAddress = address
                    MyApp.destLat = centerLatLng!!.latitude.toString()
                    MyApp.destLong = centerLatLng!!.longitude.toString()
                    openActivityAndFinish(CategoryActivity::class.java)
                } else {
                    place = CreateTripModel.Places()
                    place.place_name = placename
                    place.address = address
                    place.lat = centerLatLng!!.latitude.toString()
                    place.long = centerLatLng!!.longitude.toString()

                    /*  MyApp.driverRegistermodel.address = address
                      MyApp.driverRegistermodel.lat = centerLatLng!!.latitude.toString()
                      MyApp.driverRegistermodel.long = centerLatLng!!.longitude.toString()
                      MyApp.driverRegistermodel.place= placename
      */
                    val intent = Intent()
                    intent.putExtra(ADDRESS_CONTAINER, address)
                    intent.putExtra(LAT_CONTAINER, centerLatLng!!.latitude.toString())
                    intent.putExtra(LONG_CONTAINER, centerLatLng!!.longitude.toString())
                    intent.putExtra(PLACE_NAME_CONTAINER, placename)
                    setResult(RESULT_OK, intent)
                    finish()
                    onBackPressed()
                }

            }
        }

    }
}

