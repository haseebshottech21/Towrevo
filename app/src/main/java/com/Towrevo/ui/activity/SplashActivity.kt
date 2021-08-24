package com.Towrevo.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.LabelViewModel
import com.Towrevo.util.COMPANY
import com.Towrevo.util.CUSTOMER
import com.Towrevo.util.FAILURE
import com.Towrevo.util.SUCCESS
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {


    private val labelViewModel: LabelViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { LabelViewModel(this, mainSplashLL, noInternetView) }).get(
            LabelViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

     //   window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        window.decorView.systemUiVisibility =
//            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//               or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

    }

    private fun setData() {
        /*    Handler().postDelayed({
                openActivity(SignInActivity::class.java)
                finish()
            }, 3000)*/

        callLabelAPI()
        callGeneralAPI()
        handleLabelAPI()
    }

    private fun handleLabelAPI() {
        labelViewModel.labelResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    //toast(it[0].message)
                    MyApp.pref[PREF_SERVER_TIME] = it[0].updated_date
                    setResultLabels(it[0].result[0])
//                      isLabelAPI = true
//                    callGeneralApi()
////                    toast("label called")
                }
                FAILURE -> {
                    toast(it[0].message)
//                    callGeneralApi()
                }
                /* INVALID_TOKEN -> {
                     toast(it[0].message)
                 }*/

            }
        })
        labelViewModel.generalResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    MyApp.pref[PREF_SETTING_EMAIL] = it[0].data[0].email
                    MyApp.pref[PREF_APP_STORE_LINK] = it[0].data[0].applestoreLink
                    MyApp.pref[PREF_COPYRIGHT] = it[0].data[0].copyright
                    MyApp.pref[PREF_INSTAGRAM] = it[0].data[0].instagramLink
                    MyApp.pref[PREF_FACEBOOK] = it[0].data[0].facebookLink
                    MyApp.pref[PREF_SETTING_MOBILE] = it[0].data[0].mobile
                    MyApp.pref[PREF_PLAY_STORE] = it[0].data[0].playstoreLink

                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }
        })
    }

    private fun setResultLabels(hashmpList: HashMap<String, String>) {

        for ((key, value) in hashmpList) {
            MyApp.prefLabel[key] = value
        }

        if (MyApp.preflogin.getBoolean(PREF_IS_LOGIN, false)) {
            if (PreferenceHelper.getUserType() == CUSTOMER) {
                openActivity(CategoryActivity::class.java)
                finish()
            } else if (PreferenceHelper.getUserType() == COMPANY) {
                openActivity(CompanyHomeActivity::class.java)
                finish()
            }
        } else {
            openActivity(SignInActivity::class.java)
            finish()
        }

    }

    private fun callLabelAPI() {
        val hashMap = HashMap<String, Any>()
        labelViewModel.callLabelApi(hashMap)
    }
    private fun callGeneralAPI() {
        val hashMap = HashMap<String, Any>()
        labelViewModel.callGeneralApi(hashMap)
    }

    private fun setLabel() {

    }
}