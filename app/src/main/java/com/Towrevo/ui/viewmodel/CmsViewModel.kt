package com.Towrevo.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CmsDataModel


class CmsViewModel(val context: Context): ViewModel() {

    val cmsResponseLiveData = MutableLiveData<ArrayList<CmsDataModel>>()

    fun callCmsApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.doGetCms(hashMap), cmsResponseLiveData, showLoading = true
        )
    }


}