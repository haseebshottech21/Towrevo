package com.Towrevo.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CarTowsModel

class CarTowsViewModel(val context: Context) : ViewModel() {



    val carTowsResponseLiveData = MutableLiveData<ArrayList<CarTowsModel>>()




    fun callCarTowsApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetCarTows(hashMap),carTowsResponseLiveData)
    }

}