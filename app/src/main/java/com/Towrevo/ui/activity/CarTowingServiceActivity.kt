package com.Towrevo.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.ui.datamodel.NearByCompanyModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import kotlinx.android.synthetic.main.activity_car_towing_service.*
import kotlinx.android.synthetic.main.titlebar_layout.*

class CarTowingServiceActivity : AppCompatActivity(),View.OnClickListener {

//    val mobileNumber = ""
    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var companyid = ""
    private lateinit var companyDetails: NearByCompanyModel.Data
    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_towing_service)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        backIV.setOnClickListener { finish() }
        if (intent.hasExtra(COMPANY_CONTAINER)){
            companyDetails=intent.getSerializableExtra(COMPANY_CONTAINER) as NearByCompanyModel.Data
            carTowingServiceTV.text=companyDetails.companyName
            timeTV.text=companyDetails.workingHours
            kmTV.text= companyDetails.distance +" Miles"
            aboutDesTV.text=companyDetails.companyDetails
            mailTV.text=companyDetails.email
            callTV.text=companyDetails.mobile
            addressTV.text=companyDetails.place.address

            Glide.with(this).load(companyDetails.companyProfileImage)
                .placeholder(R.drawable.ic_company_image).into(profileImageIV)




            companyid=companyDetails.id

        }
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String


        }

    }
    private fun setData(){
        sendInquiryBT.setOnClickListener(this)
        getDirectionTV.setOnClickListener(this)
        callTV.setOnClickListener(this)
        mailTV.setOnClickListener(this)
    }
    private fun setLabel(){
        titleBarTV.text = LabelUtils.getLabel(this,CAR_TOWING_SERVICE,R.string.car_towing_service)
        carTowingServiceTV.text = LabelUtils.getLabel(this,CAR_TOWING_SERVICE,R.string.car_towing_service)
        aboutTV.text = LabelUtils.getLabel(this, ABOUT,R.string.about)
        contactTV.text = LabelUtils.getLabel(this, CONTACT,R.string.contact)
        getDirectionTV.text = LabelUtils.getLabel(this,GET_DIRECTION,R.string.get_direction)
        sendInquiryBT.text = LabelUtils.getLabel(this,SEND_INQUIRY,R.string.send_inquiry)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.sendInquiryBT -> {
                openActivity(InquiryActivity::class.java) {
                    putString(ADDRESS_CONTAINER, address)
                    putString(LAT_CONTAINER, sourceLat)
                    putString(LONG_CONTAINER, sourceLong)
                    putString(COMPANY_CONTAINER, companyid)
                }
            }
            R.id.getDirectionTV -> openActivity(GetDirectionActivity::class.java){
                putSerializable(COMPANAY_DETAIL_CONTAINER,companyDetails)
            }
            R.id.callTV -> {
                val intent = Intent()
                intent.action = Intent.ACTION_DIAL // Action for what intent called for
                intent.data =
                        Uri.parse("tel: ${companyDetails.mobile}") // Data with intent respective action on intent
                startActivity(intent)
            }
            R.id.mailTV -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(companyDetails.email)
                }
                startActivity(Intent.createChooser(emailIntent, "Send feedback"))
            }
        }
    }
}
