package com.Towrevo.ui.cms

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.labels.LabelUtils
import com.Towrevo.labels.TERMS_CONDITION
import com.Towrevo.network.PARAM_CMS_ID
import com.Towrevo.network.PARAM_TOKEN
import com.Towrevo.network.PARAM_USER_ID
import com.Towrevo.network.PARAM_USER_TYPE
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CmsViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_term_condition.*
import kotlinx.android.synthetic.main.titlebar_layout.*

class TermConditionActivity : AppCompatActivity(),View.OnClickListener {

    var content: String = ""
    var title: String = ""

    private val cmsViewModel: CmsViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { CmsViewModel(this) }).get(
            CmsViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_condition)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setTitleBar()
        callcmsAPI()
        handleAPI()
        setData()
    }
    private fun setData() {
        //loadWebView()
    }
    private fun callcmsAPI() {

        val hashMap = HashMap<String, Any>()
        hashMap[PARAM_CMS_ID] = TWO
//        hashMap[PARAM_USER_ID] = PreferenceHelper.getUserID()
//        hashMap[PARAM_TOKEN] = PreferenceHelper.getToken()
//        hashMap[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        cmsViewModel.callCmsApi(hashMap)
    }
    private fun handleAPI() {
        cmsViewModel.cmsResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    title = it[0].data[0].title
                    content = it[0].data[0].description
                    loadWebView(content)
                    titleBarTV.setText(title)
                }
                FAILURE -> toast(it[0].message)
            }
        }
        )

    }

    private fun loadWebView(content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val string = Html.fromHtml("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            termsconditionWV.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
        } else {
            val string = Html.fromHtml("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").toString()
            privacypolicyWV.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
        }
    }

    private fun setTitleBar() {
        /* title bar*/
        titleBarTV.text= LabelUtils.getLabel(this, TERMS_CONDITION, R.string.term_condition)
        searchIV.visibility= View.GONE
        backIV.visibility= View.VISIBLE
        backIV.setOnClickListener(this)
        /**********************/
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIV ->finish()
        }
    }
}
