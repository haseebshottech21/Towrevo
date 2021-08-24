package com.Towrevo.ui.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.LabelModel
import com.Towrevo.ui.datamodel.generalModel

class LabelViewModel(val context: Context, val mainView: View, val noInternetView: View) :
        ViewModel() {
    val labelResponseLiveData = MutableLiveData<ArrayList<LabelModel>>()
    val generalResponseLiveData = MutableLiveData<ArrayList<generalModel>>()

    fun callLabelApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetlabel(hashMap),labelResponseLiveData,false)
    }
    fun callGeneralApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetSetting(hashMap),generalResponseLiveData,false)
    }

    }
