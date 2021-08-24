package com.Towrevo.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CategoryDataModel
import com.Towrevo.ui.datamodel.CompanyInquiryListModel
import com.Towrevo.ui.datamodel.SignInModel

class CategoryLogoutViewModel(val context: Context) : ViewModel() {
    val logOutResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()
    val categoryDataResponseLiveData = MutableLiveData<ArrayList<CategoryDataModel>>()
    val companyInquiryListResponseLiveData = MutableLiveData<ArrayList<CompanyInquiryListModel>>()

    fun callLogoutApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetLogout(hashMap), logOutResponseLiveData, true)
    }


    fun callCategoryDataApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetCategory(hashMap), categoryDataResponseLiveData, true)
    }

    fun callCompanyInquiryListApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetCompanyInquiryList(hashMap), companyInquiryListResponseLiveData, true)
    }


}