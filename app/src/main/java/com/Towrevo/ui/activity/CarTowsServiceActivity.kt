package com.Towrevo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.labels.CAR_TOWING_SERVICE
import com.Towrevo.labels.LabelUtils
import com.Towrevo.network.PARAM_ID
import com.Towrevo.network.PARAM_TOKEN
import com.Towrevo.network.PARAM_USER_ID
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.adapter.AdapterCarImage
import com.Towrevo.ui.datamodel.CarTowsModel
import com.Towrevo.ui.datamodel.CustomerInquiryListModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CarTowsViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.toast
import com.Towrevo.util.extension.visible
import kotlinx.android.synthetic.main.activity_car_tows_service.*
import kotlinx.android.synthetic.main.activity_car_tows_service.callTV
import kotlinx.android.synthetic.main.activity_car_tows_service.mailTV
import kotlinx.android.synthetic.main.titlebar_layout.*
import java.util.HashMap

class CarTowsServiceActivity : AppCompatActivity() {
    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var companyid = ""
    private lateinit var adapterInquiryImage: AdapterCarImage
    private lateinit var CarDetail: CarTowsModel.Data
  //  private lateinit var carTowsDetails: CarTowsModel.Data
    private lateinit var carTowsDetails: CustomerInquiryListModel.Data

    private val carTowsViewModel: CarTowsViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { CarTowsViewModel(this) }).get(
            CarTowsViewModel::class.java
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_tows_service)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        handlApi()

        backIV.setOnClickListener { finish() }
        if (intent.hasExtra(COMPANY_CONTAINER)){
            carTowsDetails=intent.getSerializableExtra(COMPANY_CONTAINER) as CustomerInquiryListModel.Data
            companyid=carTowsDetails.id
            callCarTowsApi()
        }
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String
        }

    }


    private fun handlApi() {
        carTowsViewModel.carTowsResponseLiveData.observe(this, androidx.lifecycle.Observer {
            when (it[0].code) {
                SUCCESS -> {

                    CarDetail = it[0].data
                    mainLL.visible()
                    carTowingServiceTV.text=it[0].data.companyName
                    mailTV.text=it[0].data.email
//                    callTV.text=it[0].data.mobile
                    callTV.setText(PreferenceHelper.getUserMobile())
                    descriptionTV.text=it[0].data.description

                    var imageList: MutableList<CarTowsModel.Data.Image> = arrayListOf()
                    imageList.addAll(CarDetail.image)
                    adapterInquiryImage = AdapterCarImage(this, imageList)

                    CarimagesREC.adapter = adapterInquiryImage

                    Glide.with(this).load(it[0].data.companyProfileImage)
                        .placeholder(R.drawable.ic_company_image).into(profileIV)
                }
                FAILURE -> {
                    toast(it[0].message)

                }
                INVALID_TOKEN -> {
                    toast(it[0].message)
                }
            }
        })
    }

    private fun callCarTowsApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_ID] = companyid
        carTowsViewModel.callCarTowsApi(map)
    }

    private fun setData(){

    }
    private fun setLabel(){
        titleBarTV.text = LabelUtils.getLabel(this, CAR_TOWING_SERVICE,R.string.car_towing_service)
    }
}