package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.NearByCompanyModel

class NearByCompanyViewModel(val context: Context, val mainView: View, val noInternetView: View) : ViewModel() {
    val nearBycompanyResponseLiveData = MutableLiveData<ArrayList<NearByCompanyModel>>()

    fun callNearByCompanyApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetNearByCompany(hashMap), nearBycompanyResponseLiveData, true)
    }

}