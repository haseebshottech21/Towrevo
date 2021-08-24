//package com.Towrevo.ui.activity
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.Towrevo.R
//import com.Towrevo.application.MyApp
//import com.Towrevo.labels.*
//import com.Towrevo.labels.EMAIL
//import com.Towrevo.network.*
//import com.Towrevo.preference.*
//import com.Towrevo.ui.viewmodel.BaseViewModelFactory
//import com.Towrevo.ui.viewmodel.ContactUsViewModel
//import com.Towrevo.util.FAILURE
//import com.Towrevo.util.SUCCESS
//import com.Towrevo.util.Validation
//import com.Towrevo.util.extension.openActivity
//import com.Towrevo.util.extension.toast
//import kotlinx.android.synthetic.main.activity_contact_form.*
//import kotlinx.android.synthetic.main.titlebar_layout.*
//
//class Contact_form : AppCompatActivity(),View.OnClickListener {
//
//
//    private val contactUsViewModel: ContactUsViewModel by lazy {
//        ViewModelProvider(
//            this,
//            BaseViewModelFactory {
//                ContactUsViewModel(
//                    this,
//                    mainLL,
//                    noInternetView
//                )
//            }).get(
//            ContactUsViewModel::class.java
//        )
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_contact_form)
//        setData()
//        setLabel()
//        handleApi()
//    }
//
//    private fun setData() {
//        submitBT.setOnClickListener(this)
//        backIV.setOnClickListener(this)
//    }
//
//
//    private fun setLabel() {
//        titleBarTV.text = LabelUtils.getLabel(this, CONTACT_US, R.string.contact_us)
//        nameContactTV.text = LabelUtils.getLabel(this, NAME, R.string.name)
//        emailContactTV.text = LabelUtils.getLabel(this, EMAIL, R.string.email)
//        phoneContactTV.text = LabelUtils.getLabel(this, PHONE, R.string.phone)
//        messageContactTV.text = LabelUtils.getLabel(this, MESSAGE, R.string.message)
//        submitBT.text = LabelUtils.getLabel(this, SUBMIT, R.string.submit)
//    }
//
//    private fun handleApi() {
//        contactUsViewModel.contactusResponseLiveData.observe(this, Observer {
//            when (it[0].code) {
//                SUCCESS -> {
//                    MyApp.preflogin[PREF_LOGIN_TYPE] = ""
//                    MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
//                    MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
//                    MyApp.preflogin[PREF_TOKEN] = it[0].data[0].token
//                    MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
//                    MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
//                    MyApp.preflogin[PREF_NAME] = it[0].data[0].name
//                    MyApp.preflogin[PREF_MESSAGE] = it[0].data[0].message
//
//                    openActivity(CompanyHomeActivity::class.java)
//                }
//                FAILURE -> {
//                    toast(it[0].message)
//                }
//            }
//        })
//    }
//
//    private fun callContactApi() {
//        val map: HashMap<String, Any> = HashMap()
//        map[PARAM_TOKEN] = PreferenceHelper.getToken()
//        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
//        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
//        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
//        map[PARAM_DEVICE_TYPE] = "1"
//        map[PARAM_NAME] = nameET.text.toString().trim()
//        map[PARAM_EMAIL] = emailET.text.toString().trim()
//        map[PARAM_MOBILE] = phoneET.text.toString().trim()
//        map[PARAM_MESSAGE] = messageET.text.toString().trim()
//        contactUsViewModel.callContactApi(map)
//    }
//
//    private fun isValidDetail(): String {
//        var msg = ""
//
//        when {
//            nameET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_NAME,
//                    getString(R.string.name_can_not_blank)
//                )
//
//            }
//
//            emailET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_EMAILADDRESS,
//                    getString(R.string.emailaddress_cant_blank)
//                )
//
//            }
//            !Validation.isEmailValid(emailET.text.toString()) -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_VALID_EMAILADDRESS,
//                    getString(R.string.email_address_isvalid)
//                )
//
//            }
//
//            phoneET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_PHONE,
//                    getString(R.string.phone_can_not_blank)
//                )
//
//            }
//            !Validation.isPhoneValid(phoneET.text.toString()) -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_VALID_PHONE_NUMBER,
//                    getString(R.string.phone_should_valid)
//                )
//
//            }
//
//
//            messageET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(
//                    this,
//                    PLEASE_ENTER_MESSAGE,
//                    getString(R.string.message_can_not_blank)
//                )
//
//            }
//
//
//        }
//        return msg
//
//
//    }
//
//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.submitBT -> {
//                val msg = isValidDetail()
//                if (msg.isEmpty()) {
//                    callContactApi()
//                }
//                else {
//                    toast(msg)
//                }
//            }
//            R.id.backIV -> {
//                finish()
//            }
//        }
//    }
//}