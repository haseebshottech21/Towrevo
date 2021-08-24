
package com.Towrevo.ui.activity


import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.network.PARAM_COUNTRY_CODE
import com.Towrevo.network.PARAM_MOBILE
import com.Towrevo.preference.PREF_COUNTRY_CODE
import com.Towrevo.preference.PREF_USER_ID
import com.Towrevo.preference.PREF_USER_TOKEN
import com.Towrevo.preference.PREF_USER_TYPE
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.ForgotPasswordViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_forgetpassword.*
import kotlinx.android.synthetic.main.activity_forgetpassword.mainLL
import kotlinx.android.synthetic.main.activity_forgetpassword.mobileNumberTV
import kotlinx.android.synthetic.main.activity_forgetpassword.noInternetView
import kotlinx.android.synthetic.main.titlebar_layout.*

class ForgetPasswordActivity : AppCompatActivity(),View.OnClickListener {
    var otp = ""
    private var mobile = ""
//    private var otp = ""
    private var

        countryCode: String = ""

    private val forgetPasswordViewModel: ForgotPasswordViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { ForgotPasswordViewModel(this, mainLL, noInternetView) }).get(
            ForgotPasswordViewModel::class.java
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        setContentView(R.layout.activity_forgetpassword)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setLable()
        setData()
        onClick()
        handleAPI()
        titleBarTV.visibility = View.GONE
    }

    private fun onClick() {
        backIV.setOnClickListener {
            onBackPressed()
        }
        sendBTN.setOnClickListener {
            val msg = isValidDetail()
            if (msg.isEmpty()){
                //call api
                callForgotPassApi()
            }
            else{
                toast(msg)
            }
//            openActivity(OtpVerificationActivity::class.java)
        }

    }

    private fun setData() {
       sendBTN.setOnClickListener(this)
        countryCode = "1"


        phoneNumberET.setText("+" +1+" ")

        phoneNumberET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                if (!s.toString().startsWith("+" +1+" ")) {
                    phoneNumberET.setText("+" +1+" ")
                    Selection.setSelection(phoneNumberET.getText(), phoneNumberET.getText()!!.length)
                }
            }
        })
    }

    private fun setLable() {
        forgetPasswordTV.text = LabelUtils.getLabel(this, FORGET_PASSWORD1,R.string.forget_password1)
        sendBTN.text = LabelUtils.getLabel(this, SEND,R.string.send)
        mobileNumberTV.text = LabelUtils.getLabel(this, MOBILE_NUMBER,R.string.phone_number)
        pleaseEnterYourNumberToRestPasswordTV.text = LabelUtils.getLabel(this, PLEASE_ENTER_YOUR_NUMBER_TO_RESET_YOUR_PASSWORD,R.string.please_enter_your_mobile_number_to_reset_your_password)
    }
    private fun isValidDetail(): String {
        var msg=""

        when {

     //      mobile == "" -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_PHONE,
//                    getString(R.string.phone_can_not_blank)
//                )
//
//            }

            phoneNumberET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_PHONE, getString(R.string.phone_can_not_blank))
            }


        }
        return msg
    }

    private fun callForgotPassApi() {
        if (phoneNumberET.text.toString().trim().contains(" ")) {
            val numberArray = phoneNumberET.text.toString().trim().split(" ")
            mobile = numberArray[numberArray.lastIndex]
            countryCode
        } else {
            mobile = phoneNumberET.text.toString().trim()
        }
        val map: HashMap<String, Any> = HashMap()
        map[PARAM_MOBILE] = "+"+countryCode+" "+mobile
        map[PARAM_COUNTRY_CODE] = countryCode
        forgetPasswordViewModel.callForgotPasswordApi(map)

    }

    private fun handleAPI() {
        forgetPasswordViewModel.forgotpasswordResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    if (TOAST_ENABLED) {
                        // toast(it.message)
                        toast(it[0].message)
                    }
                    MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                    MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                    MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token
                    MyApp.preflogin[PREF_COUNTRY_CODE] = it[0].data[0].countryCode


                    openActivity(OtpVerificationActivity::class.java) {
                        putString(OTP_CONTAINER, it[0].data[0].otp)
                        putSerializable(OTP_ENUM_CONTAINER,OtpTypeEnum.OTP_FORGOT_PASSWORD)
                    }

//                    if (TOAST_ENABLED) {
//                        // toast(it.message)
//                        toast(it[0].message)
//                    }
//                    openActivity(OtpVerificationActivity::class.java) {
////                        putString(OTP_CONTAINER,it[0].data[0].otp)
//                    }
                }
                FAILURE->{
                    toast(it[0].message)
                }
                USER_NOT_FOUND->{
                    toast(it[0].message)
                }

            }
        }
        )
    }
    override fun onClick(v: View?) {

    }
}