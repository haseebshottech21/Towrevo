package com.Towrevo.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Towrevo.network.apiInterface
import com.Towrevo.network.callApi
import com.Towrevo.ui.datamodel.FaqModel


class FaqViewModel(val context: Context) : ViewModel() {

    /*************** Faq Api ***************/

    val faqLiveData = MutableLiveData<ArrayList<FaqModel>>()

    fun callFaqApi(hashMap: HashMap<String, Any>) {
        context.callApi(apiInterface.getFaqList(hashMap), faqLiveData)
    }

}
