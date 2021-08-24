package com.Towrevo.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.network.PARAM_ID
import com.Towrevo.network.PARAM_TOKEN
import com.Towrevo.network.PARAM_USER_ID
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.adapter.AdapterInquiryImage
import com.Towrevo.ui.datamodel.CompanyInquiryListModel
import com.Towrevo.ui.datamodel.CustomerDetailsModel
import com.Towrevo.ui.datamodel.ImageGridModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CustomerDetailsViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import com.Towrevo.util.extension.visible
import kotlinx.android.synthetic.main.activity_customer_detail.*
import kotlinx.android.synthetic.main.activity_customer_detail.aboutTV
import kotlinx.android.synthetic.main.activity_customer_detail.callTV
import kotlinx.android.synthetic.main.activity_customer_detail.contactTV
import kotlinx.android.synthetic.main.activity_customer_detail.getDirectionTV
import kotlinx.android.synthetic.main.activity_customer_detail.imagesREC
import kotlinx.android.synthetic.main.activity_customer_detail.mailTV
import kotlinx.android.synthetic.main.titlebar_layout.*
import java.util.HashMap

class CustomerDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var mobileNumber = ""
    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var customerId = ""
    private var imageList: MutableList<ImageGridModel> = arrayListOf()
    private lateinit var adapterInquiryImage: AdapterInquiryImage
    private lateinit var companyInquiryListDetails: CompanyInquiryListModel.Data
    private lateinit var customerDetail: CustomerDetailsModel.Data

    private val customerDetailsViewModel: CustomerDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { CustomerDetailsViewModel(this) }).get(
            CustomerDetailsViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detail)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        setBlockList()
        handlApi()
        backIV.setOnClickListener { finish() }

        if (intent.hasExtra(COMPANY_CONTAINER)) {
            companyInquiryListDetails =
                intent.getSerializableExtra(COMPANY_CONTAINER) as CompanyInquiryListModel.Data
            /*   userNameTV.text=customerInquiryListDetails.userName
               dateOfInquiryTV.text=customerInquiryListDetails.date
               callTV.text=customerInquiryListDetails.mobile*/


//            aboutInquiryTV.text=customerInquiryListDetails.
//            profileImageIV.text=customerInquiryListDetails.
//            fullAddressTV.text=customerInquiryListDetails.place.address

            customerId = companyInquiryListDetails.id
            callCustomerDetailsApi()

        }
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String
        }
    }

    private fun setBlockList() {
        imageList.add(
            ImageGridModel("","", true)
        )


    }
    private fun handlApi() {
        customerDetailsViewModel.customerDetailsResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it[0].code) {
                    SUCCESS -> {
                        mainLayoutLL.visible()
                        customerDetail = it[0].data

                        userNameTV.text = it[0].data.userName
                        dateOfInquiryTV.text = it[0].data.date
                        callTV.text = it[0].data.mobile
                        mobileNumber = it[0].data.mobile
                        mailTV.text = it[0].data.email
                        fullAddressTV.text = it[0].data.place.address
                        aboutInquiryTV.text = it[0].data.about
                        val imageList: MutableList<CustomerDetailsModel.Data.Image> = arrayListOf()
                        imageList.addAll(customerDetail.image)
                        adapterInquiryImage = AdapterInquiryImage(this, imageList,itemClickedCallback = {
                            openActivity(FullImageActivity::class.java){
                                putString(IMAGE_DETAIL_CONTAINER,imageList[it].image)
                            }
                        })

                        imagesREC.adapter = adapterInquiryImage

                        Glide.with(this).load(it[0].data.profileImage)
                            .placeholder(R.drawable.ic_group_image).into(profileImageIV)

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

    private fun callCustomerDetailsApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_ID] = customerId
        customerDetailsViewModel.callCustomerDetailsApi(map)
    }

    private fun setData() {
        callTV.setOnClickListener(this)
        mailTV.setOnClickListener(this)
        getDirectionTV.setOnClickListener(this)
    }

    private fun setLabel() {
        titleBarTV.text = LabelUtils.getLabel(this, CUSTOMER_DETAILS, R.string.customer_details)
        aboutTV.text = LabelUtils.getLabel(this, MESSAGE, R.string.message)
        imagesTV.text = LabelUtils.getLabel(this, IMAGE, R.string.images)
        contactTV.text = LabelUtils.getLabel(this, CONTACT, R.string.contact)
        getDirectionTV.text = LabelUtils.getLabel(this, GET_DIRECTION, R.string.get_direction)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.getDirectionTV -> openActivity(GetDirectionActivity::class.java) {
                putSerializable(CUSTOMER_DETAIL_CONTAINER, customerDetail)
            }
            R.id.callTV -> {
                val intent = Intent()
                intent.action = Intent.ACTION_DIAL // Action for what intent called for
                intent.data =
                    Uri.parse("tel: ${companyInquiryListDetails.mobile}") // Data with intent respective action on intent
                startActivity(intent)
            }
            R.id.mailTV -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:abc@xyz.com")
                }
                startActivity(Intent.createChooser(emailIntent, "Send feedback"))
            }
        }
    }
}