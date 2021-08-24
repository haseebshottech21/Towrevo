package com.Towrevo.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.Towrevo.R
import com.Towrevo.labels.LabelUtils
import com.Towrevo.labels.TOWING_COMPANIES
import com.Towrevo.network.*
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.adapter.AdapterTowingCompanies
import com.Towrevo.ui.datamodel.NearByCompanyModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.NearByCompanyViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.gone
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.visible
import com.twoSecure.util.LocationUtils
import kotlinx.android.synthetic.main.activity_towing_companies.*
import kotlinx.android.synthetic.main.activity_towing_companies.listViewwIV
import kotlinx.android.synthetic.main.activity_towing_companies.noInternetView
import kotlinx.android.synthetic.main.item_towing_companies_layout.*

import kotlinx.android.synthetic.main.titlebar_layout.*
import java.util.HashMap

class TowingCompaniesActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener,
    LocationListener {
    private var firmlist: MutableList<NearByCompanyModel.Data> = arrayListOf()
    private var googleMap: GoogleMap? = null
    private var source = ""
    private var address = ""
    private var sourceLat = ""
    private var sourceLong = ""
    private var categoryId = ""
    private var isMap: Boolean = false

    //    private var mobileNumber = ""
    private lateinit var marker: Marker
    private lateinit var companyDetail: NearByCompanyModel.Data

    private val nearByCompanyViewModel: NearByCompanyViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { NearByCompanyViewModel(this, towing, noInternetView) }).get(
            NearByCompanyViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_towing_companies)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        setUpMap()
        backIV.setOnClickListener { finish() }
        searchIV.visibility = View.VISIBLE
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String
            categoryId = intent.getStringExtra(CATEGORY_CONTAINER) as String
            //toast(categoryId)
//            mobileNumber = intent.getStringExtra(MOBILE_CONTAINER)as String
            callNearByApi()

        }
        handleApi()

    }

    private fun handleApi() {
        nearByCompanyViewModel.nearBycompanyResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it[0].code) {
                    SUCCESS -> {
                        towing.visible()
                        firmlist.addAll(it[0].data)
                        setScanList()
                        setmarkerData(firmlist)
                    }
                    NO_DATA_FOUND -> {
                        noDataFoundTv.visible()
                    }
                }
            })
    }

    private fun setmarkerData(tripRequests: MutableList<NearByCompanyModel.Data>) {
        for (i in tripRequests.indices) {
            val triplat: Double = tripRequests[i].place.latitude.toDouble()
            val triplong: Double = tripRequests[i].place.longitude.toDouble()
            val tripLatLng = LatLng(triplat, triplong)
            // val latLng=tripRequests[i].pickupLocation.lat+tripRequests[i].pickupLocation.long
            val markerOptions = MarkerOptions()
            markerOptions.position(tripLatLng)
            markerOptions.icon(
                BitmapDescriptorFactory.fromResource(R.drawable.pin1)
            )
//            markerOptions.title(tripRequests[i].pickupLocation.address)
            // googleMap!!.clear()
            //  googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(tripLatLng))

            marker = googleMap!!.addMarker(markerOptions)
            marker.tag = tripRequests[i]
            marker.title = tripRequests[i].companyName
            marker.showInfoWindow()
            googleMap!!.addMarker(markerOptions)
        }

        companyDetail = marker.tag as NearByCompanyModel.Data
        Log.e("Data", "${companyDetail}")

/*
        if (PreferenceHelper.getDriverType().equals(com.twosecure.App.util.TOUR_DRIVER)) {
            showTourDetailsBottomSheet(tripData)
        } else {
            showTripDetailsBottomSheet(tripData)
        }
*/

    }


    private fun callNearByApi() {

        val map: HashMap<String, Any> = HashMap()

        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_CATEGORY_ID] = categoryId
        map[PARAM_ADDRESS] = address
        map[PARAM_PAGE] = PreferenceHelper.getPage()
        map[PARAM_LONGITUDE] = sourceLong
        map[PARAM_LATITTUDE] = sourceLat
        nearByCompanyViewModel.callNearByCompanyApi(map)

    }


    private fun setData() {
        searchIV.setOnClickListener(this)
        backArrowIV.setOnClickListener(this)
        //      callIV.setOnClickListener(this)
        listViewwIV.setOnClickListener(this)

    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setLabel() {
        titleBarTV.text = LabelUtils.getLabel(this, TOWING_COMPANIES, R.string.towing_companies)

    }

    private fun setScanList() {

        val scanListAdapter = AdapterTowingCompanies(this, firmlist, callCompanyCallback = {

            val intent = Intent()
            intent.action = Intent.ACTION_DIAL // Action for what intent called for
            intent.data =
                Uri.parse("tel: ${firmlist[it].mobile}") // Data with intent respective action on intent
            startActivity(intent)


        }, itemClickCallback = {
            openActivity(CarTowingServiceActivity::class.java) {
                putSerializable(COMPANY_CONTAINER, firmlist[it])
                putString(ADDRESS_CONTAINER, address)
                putString(LAT_CONTAINER, sourceLat)
                putString(LONG_CONTAINER, sourceLong)
                putString(CATEGORY_CONTAINER, categoryId)
            }
        }, dirctionCallback = {

            openActivity(GetDirectionActivity::class.java) {
                putSerializable(COMPANAY_DETAIL_CONTAINER, firmlist[it])
            }

        })
        towingCompaniesREC.adapter = scanListAdapter
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrowIV -> {
                towing.visibility = View.VISIBLE
                location.visibility = View.GONE
            }
            R.id.searchIV -> {
                isMap = true
                location.visibility = View.VISIBLE
                towing.visibility = View.GONE
            }
            R.id.listViewwIV -> {

                towing.visibility = View.VISIBLE
                location.visibility = View.GONE
            }
            R.id.callIV -> {
//                val intent = Intent()
//                intent.action = Intent.ACTION_DIAL // Action for what intent called for
//                intent.data =
//                    Uri.parse("tel: ${firmlist[0].mobile}") // Data with intent respective action on intent
//                startActivity(intent)
//            }

            }

        }
    }

    override fun onBackPressed() {

        if (isMap) {
            isMap = false
            location.gone()
            towing.visible()
        } else {
            super.onBackPressed()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap!!.setOnMapLoadedCallback(this)
        googleMap!!.setOnMarkerClickListener(this)

        PermissionUtils.checkForPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            myCallBack = {
                if (it) {
                    googleMap!!.isMyLocationEnabled = true

                }
            })

        googleMap!!.setOnMarkerClickListener { marker ->
            companyDetail = NearByCompanyModel.Data()

            if (marker.tag != null) {
                companyDetail = marker!!.tag as NearByCompanyModel.Data
                Log.e("Data", "${companyDetail}")
                showDialog2(companyDetail)

//                toast("show Popup")
            } else {

                setmarkerData(firmlist)
                companyDetail = marker!!.tag as NearByCompanyModel.Data
                Log.e("Data", "${companyDetail}")
                showDialog2(companyDetail)

            }
            return@setOnMarkerClickListener true
        }
    }

    private fun showDialog2(companyDetail: NearByCompanyModel.Data) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setContentView(R.layout.item_towing_companies_layout)


        dialog.companyTV.text = companyDetail.companyName
        dialog.companyDetailsTV.text = companyDetail.companyDetails.trim()



        dialog.callIV.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL // Action for what intent called for
            intent.data =
                Uri.parse("tel: ${firmlist[0].mobile}") // Data with intent respective action on intent
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.getDirectionTV.setOnClickListener {
            openActivity(GetDirectionActivity::class.java) {
                putSerializable(COMPANAY_DETAIL_CONTAINER, firmlist[View.VISIBLE])
            }
            dialog.show()
        }
    }

    override fun onMapLoaded() {
        LocationUtils.setUpLocation(this, onLocationReceived = { location ->
            LocationUtils.updateCamera(googleMap!!, location.latitude, location.longitude)
        })
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }
}