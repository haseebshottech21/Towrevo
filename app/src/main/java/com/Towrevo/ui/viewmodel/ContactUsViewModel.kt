package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CustomerDetailsModel

class ContactUsViewModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {

    val contactusResponseLiveData = MutableLiveData<ArrayList<CustomerDetailsModel>>()

    fun callContactApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetContact(hashMap),contactusResponseLiveData)
    }
}