package com.Towrevo.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.R.layout.activity_otp_verification
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.authentication.BaseAuthActivity
import com.Towrevo.ui.authentication.CreateNewPasswordActivity
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.OtpVerificationViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.gone
import com.Towrevo.util.extension.openActivity
import kotlinx.android.synthetic.main.activity_otp_verification.*
import kotlinx.android.synthetic.main.activity_otp_verification.mainLL
import kotlinx.android.synthetic.main.activity_otp_verification.noInternetView
import kotlinx.android.synthetic.main.titlebar_layout.*
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


class OtpVerificationActivity : BaseAuthActivity(), View.OnClickListener {
    var otp = ""

    //    private lateinit var registerTypeEnum: RegisterTypeEnum
    var registerAs: String = ""
    var userType: String = ""
    var otpTypeEnum: OtpTypeEnum = OtpTypeEnum.OTP_REGISTER

    private val otpVerificationViewModel: OtpVerificationViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { OtpVerificationViewModel(this, mainLL, noInternetView) }).get(
            OtpVerificationViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(activity_otp_verification)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setLabel()
        startTimer()
        setData()
        titleBarTV.gone()


//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
////        showKeyboard(otp_view)

//        if (intent.hasExtra(OTP_CONTAINER)) {
//            otp = intent.getStringExtra(OTP_CONTAINER) as String
//            otpTv.text = "1234"
//        }
        if (intent.hasExtra(OTP_ENUM_CONTAINER)) {
            otpTypeEnum = intent.getSerializableExtra(OTP_ENUM_CONTAINER) as OtpTypeEnum
        }
        handleAPI()
    }

    private fun handleAPI() {
        otpVerificationViewModel.otpsendResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    otpTv.text = it[0].message
                    toast(it[0].message)

                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }
        }
        )
        //Resend Otp observer
        otpVerificationViewModel.otpVerifyResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {

                    when (it[0].data[0].userType) {
                        CUSTOMER -> {
                            //customer details
                            //MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString()
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

                            if (otpTypeEnum.equals(OtpTypeEnum.OTP_REGISTER)) {
                                openActivity(CategoryActivity::class.java)

                            } else if (otpTypeEnum.equals(OtpTypeEnum.OTP_FORGOT_PASSWORD)) {
                                openActivity(CreateNewPasswordActivity::class.java)
                            }


                        }
                        com.Towrevo.util.COMPANY -> {
                            //company details
                            // MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString()
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

                            if (otpTypeEnum.equals(OtpTypeEnum.OTP_REGISTER)) {
                                openActivity(CompanyHomeActivity::class.java)

                            } else if (otpTypeEnum.equals(OtpTypeEnum.OTP_FORGOT_PASSWORD)) {
                                openActivity(CreateNewPasswordActivity::class.java)
                            }
                        }
                    }

                }
                FAILURE -> {
                    toast(it[0].message)
                }
                ENTER_OTP_IS_INCORRECT -> {
                    toast(it[0].message)
                }
            }
        })
    }
    private fun callVerifyOtpApi() {

        var ott = otp_view.otp
        val map: HashMap<String, Any> = HashMap()
        map[PARAM_OTP] = ott.toString()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_DEVICE_TYPE] = "1"



        otpVerificationViewModel.callVerifyOtpApi(map)
    }

    private fun callResendOtpApi() {
        val map: HashMap<String, Any> = HashMap()
        map[PARAM_MOBILE] = PreferenceHelper.getUserMobile()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()

        otpVerificationViewModel.callSendOtpApi(map)

    }


//    private fun handleOtpApi() {
//        otpVerificationViewModel.otpsendResponseLiveData.observe(this, Observer {
//
//            when (it[0].code) {
//                SUCCESS -> {
//                    MyApp.preflogin[PREF_USER_ID] = it[0].result[0].toString().trim()
//                    openActivity(LoginActivity::class.java)
//                    toast(it[0].message)
//                }
//                FAILURE -> {
//                    toast(it[0].message)
//                }
//                CODE_FIVE -> {
//                    toast(it[0].message)
//                }
//
//
//            }
//        })
//    }

    fun Context.showKeyboard(
        view: View?
    ) {
        val imm =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view?.windowToken, 0, 0)
        view?.clearFocus()
    }

    private fun startTimer() {
        object : CountDownTimer(120000, 1000) {
            // adjust the milli seconds here
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                txtExpiring.text = "Expiring in  " + java.lang.String.format(
                    "%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -

                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
            }

            override fun onFinish() {
                txtExpiring.text = "Expired"
                resendEmailVerifyTV.visibility = View.VISIBLE
            }
        }.start()
    }


    private fun setData() {
        backIV.setOnClickListener(this)
        resendEmailVerifyTV.setOnClickListener(this)
        verifyBT.setOnClickListener(this)
    }

    @SuppressLint("DefaultLocale")
    private fun setLabel() {
        otpTV.text = LabelUtils.getLabel(this, OTP_VERIFICATION, R.string.otp_verification)
        txtExpiring.text = LabelUtils.getLabel(this, EXPIRING_IN, R.string.expiring_in_01_00)
        verifyBT.text = LabelUtils.getLabel(this, VERIFY, R.string.verify)
        mobile_otp.text = getString(R.string.we_have_sent_you_an_sms_with_a_code_to_the_number_33_1234_456_789) +" " + PreferenceHelper.getUserMobile()
        resendEmailVerifyTV.text = LabelUtils.getLabel(this, RE_SEND_CODE, R.string.re_send_code)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.verifyBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {
                    callVerifyOtpApi()
                } else {
                    toast(msg)

                }
            }
            R.id.resendEmailVerifyTV -> {
                startTimer()
                resendEmailVerifyTV.visibility = View.GONE
                callResendOtpApi()

            }
            R.id.backIV -> finish()
        }
    }

    /**
     * check validation
     */
    private fun isValidDetail(): String {
        var msg = ""
        when {
            otp_view.otp.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_OTP,
                    getString(R.string.please_enter_otp)
                )
            }
        }
        return msg
    }
}