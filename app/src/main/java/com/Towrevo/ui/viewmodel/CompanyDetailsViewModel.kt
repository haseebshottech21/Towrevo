package com.Towrevo.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CustomerInquiryListModel


class CompanyDetailsViewModel(val context: Context) : ViewModel() {



    val companyDetailsResponseLiveData = MutableLiveData<ArrayList<CustomerInquiryListModel>>()




    fun callCompanyDetailsApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetCompanyDetails(hashMap),companyDetailsResponseLiveData)
    }

}