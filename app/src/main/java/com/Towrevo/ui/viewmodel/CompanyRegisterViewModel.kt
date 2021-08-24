package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CategoryDataModel
import com.Towrevo.ui.datamodel.CompanyRegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody


class CompanyRegisterViewModel(val context: Context, val mainView: View, val noInternetView: View) :ViewModel() {

    val companyRegisterResponseLiveData = MutableLiveData<ArrayList<CompanyRegisterModel>>()


    val categoryDataResponseLiveData = MutableLiveData<ArrayList<CategoryDataModel>>()

    fun callCreateUserApi(hashMap: Map<String, RequestBody>, file : MultipartBody.Part) {
        context.callApi(
            apiInterface.doGetCompanyRegister(hashMap,file),
            companyRegisterResponseLiveData

        )
    }
    fun callCategoryDataApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetCategory(hashMap), categoryDataResponseLiveData, false)
    }

}