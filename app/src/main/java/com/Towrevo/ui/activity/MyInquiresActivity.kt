package com.Towrevo.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.labels.LabelUtils
import com.Towrevo.labels.MY_INQUIRIES
import com.Towrevo.network.PARAM_PAGE
import com.Towrevo.network.PARAM_TOKEN
import com.Towrevo.network.PARAM_USER_ID
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.adapter.AdapterMyInquireies
import com.Towrevo.ui.datamodel.CustomerInquiryListModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CompanyDetailsViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.visible
import kotlinx.android.synthetic.main.activity_my_inquires.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import java.util.HashMap

class MyInquiresActivity : AppCompatActivity(),View.OnClickListener {
    private var companyList: MutableList<CustomerInquiryListModel.Data> = arrayListOf()
    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var companyid = ""


    private val customerDetailsViewModel: CompanyDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { CompanyDetailsViewModel(this) }).get(
            CompanyDetailsViewModel::class.java
        )
    }




    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_inquires)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        setScanList()
        handlApi()
        callCompanyListApi()
        backIV.setOnClickListener { finish() }
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String
            companyid = intent.getStringExtra(CATEGORY_CONTAINER) as String


        }
    }

    private fun handlApi() {
        customerDetailsViewModel.companyDetailsResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it[0].code) {
                    SUCCESS -> {
                        myInquiresREC.visible()
                        companyList.addAll(it[0].data)
                        setScanList()
                    }

                    NO_DATA_FOUND -> {
                        noDataFoundTv.visible()

                    }

                }
            })
    }

    private fun callCompanyListApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_PAGE] = "1"
        customerDetailsViewModel.callCompanyDetailsApi(map)
    }

    private fun setData(){
     backIV.setOnClickListener(this)
    }
    private fun setLabel(){
        titleBarTV.text = LabelUtils.getLabel(this, MY_INQUIRIES,R.string.my_inquiries)

    }

        private fun setScanList() {

            val scanListAdapter = AdapterMyInquireies(this, companyList, callCompanysCallback = {

            }, itemClickedCallback = {
                openActivity(CarTowsServiceActivity::class.java) {
                    putSerializable(COMPANY_CONTAINER, companyList[it])
                    putString(ADDRESS_CONTAINER, address)
                    putString(LAT_CONTAINER, sourceLat)
                    putString(LONG_CONTAINER, sourceLong)
                    putString(CATEGORY_CONTAINER, companyid)
                }
            })
            myInquiresREC.adapter = scanListAdapter
        }



    override fun onClick(v: View?) {

    }
}




