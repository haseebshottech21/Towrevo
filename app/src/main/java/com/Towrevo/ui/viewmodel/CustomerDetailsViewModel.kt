package com.Towrevo.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.CustomerDetailsModel

class CustomerDetailsViewModel(val context: Context) : ViewModel() {



    val customerDetailsResponseLiveData = MutableLiveData<ArrayList<CustomerDetailsModel>>()




    fun callCustomerDetailsApi(hashMap: HashMap<String,Any>) {
        context.callApi(apiInterface.doGetCustomerDetails(hashMap),customerDetailsResponseLiveData)
    }

}