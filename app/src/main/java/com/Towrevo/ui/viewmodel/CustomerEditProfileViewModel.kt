package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.SignInModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CustomerEditProfileViewModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {

    val customereditProfileResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()

    val viewProfileResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()

//    fun callCustomerEditProfileApi(hashMap: Map<String, RequestBody>, file : MultipartBody.Part) {
//        context.callApi(apiInterface.doGetCustomerEditProfile(hashMap,file),customereditProfileResponseLiveData)
//    }
    fun callCustomerEditProfileApi(hashMap: Map<String, RequestBody>, file : MultipartBody.Part) {
        context.callApi(
            apiInterface.doGetCustomerEditProfile(hashMap,file),
            customereditProfileResponseLiveData

        )
    }




    fun callViewProfileApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetViewProfile(hashMap),viewProfileResponseLiveData)
    }

}