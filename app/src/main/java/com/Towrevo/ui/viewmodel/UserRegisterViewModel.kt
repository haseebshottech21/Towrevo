package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.UserRegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRegisterViewModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {


    val userRegisterResponseLiveData = MutableLiveData<ArrayList<UserRegisterModel>>()


    fun CallUserRegisterApi(hashMap: Map<String, RequestBody>, file : MultipartBody.Part) {
        context.callApi(
            apiInterface.doGetUserRegister(hashMap,file),
            userRegisterResponseLiveData

        )
    }
}
