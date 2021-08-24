package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.SignInModel

class OtpVerificationViewModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {


    val otpsendResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()
    val otpVerifyResponseLiveData = MutableLiveData<ArrayList<SignInModel>>()

    /* fun callRegisterApi(name:String,email: String,phone:String, password: String,device_type:String,device_token:String,user_type:String) {
         context.callApi(apiInterface.doGetRegister(name,email,phone,password,device_type,device_token,user_type),registerResponseLiveData,view = mainView,noInternetView = noInternetView)
     }*/

    fun callSendOtpApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetOtp(hashMap),otpsendResponseLiveData)
    }


    fun callVerifyOtpApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetVerifyOtp(hashMap),otpVerifyResponseLiveData)
    }
}