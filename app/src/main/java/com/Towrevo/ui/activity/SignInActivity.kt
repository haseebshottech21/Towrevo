package com.Towrevo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.labels.LOGIN
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.SignInViewModel
import com.Towrevo.util.*
import com.Towrevo.util.Util.getRegisterDeviceToken
import com.Towrevo.util.Validation.isEmailValid
import com.Towrevo.util.Validation.isPhoneValid
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.eaddressET
import kotlinx.android.synthetic.main.activity_sign_in.noInternetView
import kotlinx.android.synthetic.main.activity_sign_in.signUpLoginTV

class SignInActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {

    private val signInViewModel: SignInViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { SignInViewModel(this, mainRL, noInternetView) }).get(
            SignInViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        setContentView(R.layout.activity_sign_in)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setData()
        setLable()
        handleApi()
        getRegisterDeviceToken()

//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

    }

    override fun onRestart() {
        super.onRestart()
        eaddressET.setText("")
        passwordEET.setText("")

    }


    private fun handleApi() {
        signInViewModel.signInResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    when (it[0].data[0].userType) {
                        CUSTOMER -> {
                            //customer details
                            if (it[0].data[0].isVerify == VERIFIED) {

                                MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString()
                                MyApp.preflogin[PREF_IS_LOGIN] = true
                                MyApp.preflogin[PREF_LOGIN_TYPE] = ""
                                MyApp.preflogin[PREF_FIRST_NAME] = it[0].data[0].firstName
                                MyApp.preflogin[PREF_LAST_NAME] = it[0].data[0].lastName
                                MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                                MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                                MyApp.preflogin[PREF_PROFILE_IMAGE] = it[0].data[0].profileImage
                                MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                                MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                                MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token

                                openActivity(CategoryActivity::class.java)
                            }

                        }
                        com.Towrevo.util.COMPANY -> {
                            //company details
                            if (it[0].data[0].isVerify.equals(VERIFIED)) {
                                MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString()
                                MyApp.preflogin[PREF_IS_LOGIN] = true
                                MyApp.preflogin[PREF_LOGIN_TYPE] = ""
                                MyApp.preflogin[PREF_COMPANY_NAME] = it[0].data[0].companyName
                                MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                                MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                                MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                                MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token
                                MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                                MyApp.preflogin[PREF_WORKING_HOUR] = it[0].data[0].workingHour
                                MyApp.preflogin[PREF_PROFILE_IMAGE] =
                                    it[0].data[0].companyProfileImage
                                openActivity(CompanyHomeActivity::class.java)

                            }
                        }
                    }

                }
                FAILURE -> {
                    toast(it[0].message)
                }
                INVALID_PASSWORD -> {
                    toast(it[0].message)
                }
                USER_NOT_FOUND -> {
                    toast(it[0].message)
                }
                VERIFY_YOUR_ACCOUNT -> {

                    toast(it[0].message)
                    when (it[0].data[0].userType) {
                        CUSTOMER -> {
                            MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString()
                            MyApp.preflogin[PREF_LOGIN_TYPE] = ""
                            MyApp.preflogin[PREF_FIRST_NAME] = it[0].data[0].firstName
                            MyApp.preflogin[PREF_LAST_NAME] = it[0].data[0].lastName
                            MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                            MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                            MyApp.preflogin[PREF_PROFILE_IMAGE] = it[0].data[0].profileImage
                            MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                            MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                            MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token

                            openActivity(OtpVerificationActivity::class.java) {
                                putString(OTP_CONTAINER, it[0].data[0].otp)
                            }
                        }
                        com.Towrevo.util.COMPANY -> {
                            MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString()
                            MyApp.preflogin[PREF_LOGIN_TYPE] = ""
                            MyApp.preflogin[PREF_COMPANY_NAME] = it[0].data[0].companyName
                            MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                            MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                            MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                            MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token
                            MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                            MyApp.preflogin[PREF_WORKING_HOUR] = it[0].data[0].workingHour
                            MyApp.preflogin[PREF_PROFILE_IMAGE] =
                                it[0].data[0].companyProfileImage
                            openActivity(OtpVerificationActivity::class.java) {
                                putString(OTP_CONTAINER, it[0].data[0].otp)
                            }
                            finish()
                        }


                    }
                }
            }
        })
    }


    private fun setData() {
        passwordEET.setOnTouchListener(this)
        signInBTN.setOnClickListener(this)
        dontHaveAccountTV.setOnClickListener(this)
        forgetPasswordTV.setOnClickListener(this)
        signUpLoginTV.setOnClickListener(this)
    }

    private fun setLable() {
        signInTV.text = LabelUtils.getLabel(this, LOGIN, getString(R.string.login))
        emailaddressTV.text =
            LabelUtils.getLabel(this, MOBILE_NUMBER, getString(R.string.mobile_number))
        passwordTV.text = LabelUtils.getLabel(
            this,
            com.Towrevo.labels.PASSWORD,
            getString(R.string.password)
        )

        signInBTN.text = LabelUtils.getLabel(this, LOGIN, getString(R.string.login))
        forgetPasswordTV.text =
            LabelUtils.getLabel(this, FORGET_PASSWORD, getString(R.string.forget_password))
//        dontHaveAccountTV.text = LabelUtils.getLabel(this, DONT_HAVE_AN_ACCOUNT,getString(R.string.dont_have_account))
    }
//    private fun isValidDetail(): String {
//        var msg=""
//
//        /*  eaddressET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_EMAILADDRESS, getString(R.string.emailaddress_cant_blank))
//
//            }
//            !Validation.isEmailValid(eaddressET.text.toString()) -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_VALID_EMAILADDRESS, getString(R.string.email_address_isvalid))
//
//            }
//            passwordET.text.toString().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_PASSWORD, getString(R.string.password_can_not_blank))
//
//            }
//*/
//
//            if (eaddressET.text.toString().trim().matches("[0-9]+".toRegex())) {
//                msg = isValidMobile()
//            } else {
//                msg = isValidEmail()
//            }
//
//        return msg
//    }
//    private fun isValidMobile(): String {
//
//        var msg = ""
//        var email = eaddressET.text.toString().trim()
//        when {
//            eaddressET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, EMAIL_OR_PHONE_CANT_BE_EMPTY, getString(R.string.email_phone_can_not_blank))
//
//            }
//            email.length < 10 -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_VALID_MOBILE_NUMBER, getString(R.string.please_enter_valid_mobile_number))
//
//            }
//            passwordET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_PASSWORD, getString(R.string.password_can_not_blank))
//            }
//        }
//        return msg
//    }

    private fun isValidMobile(): String {

        var msg = ""
        var email = eaddressET.text.toString().trim()
        when {
            eaddressET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_EMAIL_OR_CONTACT,
                    getString(R.string.email_phone_can_not_blank)
                )
            }

            email.length < 10 -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_MOBILE_NUMBER,
                    getString(R.string.please_enter_valid_mobile_number)
                )

            }
            passwordEET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )
            }
        }
        return msg
    }

    private fun isValidDetail(): String {

        var msg = ""

        when {
            eaddressET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PHONE,
                    getString(R.string.phone_can_not_blank)
                )

            }


            !isPhoneValid(eaddressET.text.toString().trim()) -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_MOBILE_NUMBER,
                    getString(R.string.phone_should_valid)
                )

            }


            passwordEET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )
            }
        }
        return msg
    }

    private fun callLoginApi() {
        val map: HashMap<String, Any> = HashMap()
        map[PARAM_EMAIL] = eaddressET.text.toString().trim()
        map[PARAM_PASSWORD] = passwordEET.text.toString().trim()
        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
        map[PARAM_DEVICE_TYPE] = "1"

        signInViewModel.callLoginApi(map)

    }


//    map[PARAM_COMPANY_PROFILE_IMAGE] = phoneET.text.toString().trim()
//    map[PARAM_COMPANY_NAME] = ANDROID
//    map[PARAM_ADDRESS] = PreferenceHelper.getDeviceToken()
//    map[PARAM_EMAIL] = passwordET.text.toString().trim()
//    map[PARAM_PASSWORD] = passwordET.text.toString().trim()
//    map[PARAM_ABOUT] = passwordET.text.toString().trim()
//    map[PARAM_OTP] = passwordET.text.toString().trim()
//    map[PARAM_TOKEN] = passwordET.text.toString().trim()
//    map[PARAM_USER_ID] = passwordET.text.toString().trim()
//    map[PARAM_USER_TYPE] = passwordET.text.toString().trim()
//    map[PARAM_MOBILE] = passwordET.text.toString().trim()
//    map[PARAM_WORKING_HOUR] = passwordET.text.toString().trim()
//    map[PARAM_IS_VERIFY] = passwordET.text.toString().trim()

//    private fun isValidEmail(): String {
//
//        var msg = ""
//
//        when {
//            eaddressET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, EMAIL_OR_PHONE_CANT_BE_EMPTY, getString(R.string.email_phone_can_not_blank))
//
//            }
//            !isEmailValid(eaddressET.text.toString().trim()) -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_VALID_EMAIL, getString(R.string.email_should_valid))
//
//            }
//            passwordEET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_PASSWORD, getString(R.string.password_can_not_blank))
//            }
//        }
//        return msg
//    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.passwordEET -> Validation.TansformPassword(event, passwordEET)
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signInBTN -> {
                var msg: String = ""
                msg = isValidDetail()
                if (msg.isEmpty()) {

                    /* call api */
                    callLoginApi()


                } else {
                    toast(msg)
                }
            }
//            R.id.signInBTN -> {
//                var msg = ""
//
//                if (eaddressET.text.toString().trim().matches("[0-9]+".toRegex())) {
//                    msg = isValidMobile()
//                } else {
//                    msg = isValidDetail()
//                }
//                if (msg.isEmpty()) {
//                    if (eaddressET.text.toString().trim().equals(CUSTOMR_EMAIL)) {
//                        openActivity(CategoryActivity::class.java)
//
//                    } else if (eaddressET.text.toString().trim().equals(COMPANY_EMAIL)) {
//                        openActivity(CompanyHomeActivity::class.java)
//                    }
//
//                    /* call api */
////                    loginViewModel.callLoginApi(emailET.text.toString(), passwordET.text.toString())
//                } else {
//                    toast(msg)
//                }
//            }
            R.id.forgetPasswordTV -> {
                openActivity(ForgetPasswordActivity::class.java)
            }

            R.id.signUpLoginTV -> {
                openActivity(RegisterActivity::class.java)
            }
        }
    }
}