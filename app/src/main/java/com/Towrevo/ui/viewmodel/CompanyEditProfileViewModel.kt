package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CategoryDataModel
import com.Towrevo.ui.datamodel.SignInModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CompanyEditProfileViewModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {


    val companyeditProfileResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()
    val viewProfileResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()
    val categoryDataResponseLiveData = MutableLiveData<ArrayList<CategoryDataModel>>()

    fun callCompanyEditProfileApi(hashMap: Map<String, RequestBody>, file : MultipartBody.Part) {
        context.callApi(
            apiInterface.doGetCompanyEditProfile(hashMap,file),
            companyeditProfileResponseLiveData

        )
    }

    fun callViewProfileApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetViewProfile(hashMap),viewProfileResponseLiveData)
    }

    fun callCategoryDataApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetCategory(hashMap), categoryDataResponseLiveData, false)
    }

}
