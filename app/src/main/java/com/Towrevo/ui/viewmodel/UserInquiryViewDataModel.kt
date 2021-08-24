package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.UserInquiryModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserInquiryViewDataModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {

    val userInquiryResponseLiveData = MutableLiveData<ArrayList<UserInquiryModel>>()
//    val viewProfileResponseLiveData = MutableLiveData<ArrayList<UserInquiryModel>>()

    fun callUserInquiryApi(hashMap: Map<String, RequestBody>, file : MultipartBody.Part) {
        context.callApi(
            apiInterface.doGetUserInquiry(hashMap,file),
            userInquiryResponseLiveData

        )
    }



//    fun callViewProfileApi(hashMap: HashMap<String,Any>) {
//        context.callApi(apiInterface.doGetViewProfile(hashMap),viewProfileResponseLiveData)
//    }

}