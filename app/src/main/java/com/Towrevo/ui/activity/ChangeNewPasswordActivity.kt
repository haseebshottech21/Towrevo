package com.Towrevo.ui.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.network.*
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.ChangePasswordViewModel
import com.Towrevo.util.FAILURE
import com.Towrevo.util.SUCCESS
import com.Towrevo.util.Validation
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_change_new_password.*
import kotlinx.android.synthetic.main.activity_change_new_password.mainLL
import kotlinx.android.synthetic.main.activity_change_new_password.noInternetView
import kotlinx.android.synthetic.main.titlebar_layout.*
import java.util.HashMap

class ChangeNewPasswordActivity : AppCompatActivity(),View.OnClickListener,View.OnTouchListener{


    private val changePasswordViewModel: ChangePasswordViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { ChangePasswordViewModel(this, mainLL, noInternetView) }).get(
            ChangePasswordViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        setContentView(R.layout.activity_change_new_password)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setLable()
        setData()
        handleApi()
        onClick()
    }

//    override fun getLayoutResourceId(): Int {
//        return R.layout.activity_change_new_password
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    }
//
//    override fun main() {
//       setLable()
//       setData()
//        handleApi()
//       onClick()
//    }

    private fun onClick() {
        backIV.setOnClickListener {
            onBackPressed()
        }
        resetBTN.setOnClickListener {
            val msg = isValidDetail()
            if (msg.isEmpty()){
                //api call
                callChangePasswordApi()
            }
            else{
                toast(msg)
            }
        }
    }

    private fun setData() {
        backIV.setOnClickListener(this)
        currentPasswordEET.setOnTouchListener(this)
        newPasswordEET.setOnTouchListener(this)
        confirmPasswordEET.setOnTouchListener(this)

    }

    private fun setLable() {
        titleBarTV.text = LabelUtils.getLabel(this, CHANGE_PASSWORD,R.string.change_password)
        currentPasswordEET.hint =LabelUtils.getLabel(this, CURRENT_PASSWORD ,getString(R.string.current_passwords))
        currentPasswordTV.hint =LabelUtils.getLabel(this, CURRENT_PASSWORD ,getString(R.string.current_password))
        newPasswordTV.hint =LabelUtils.getLabel(this, CURRENT_PASSWORD ,getString(R.string.new_password))
        confirmPasswordTV.hint =LabelUtils.getLabel(this, CURRENT_PASSWORD ,getString(R.string.confirm_password))
        newPasswordEET.hint =LabelUtils.getLabel(this,NEW_PASSWORD ,getString(R.string.new_passwords))
        confirmPasswordEET.hint =LabelUtils.getLabel(this,CONFIRM_PASSWORD ,getString(R.string.confirm_passwords))
        resetBTN.text =LabelUtils.getLabel(this, UPDATE ,getString(R.string.update))

    }
    private fun isValidDetail(): String {
        var msg=""

        when {
            currentPasswordEET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_CURRENT_PASSWORD, getString(R.string.current_pw_can_not_blank))

            }
            currentPasswordEET.text.toString().trim().length <= 3 -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_CURRENT_ENTER_MORE_THAN_FOUR_DIGIT_PASSWORD,
                    getString(R.string.current_password_can_not_less_then_4_blank)
                )
            }

            newPasswordEET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_NEW_PASSWORD, getString(R.string.new_pw_can_not_blank))

            }
            newPasswordEET.text.toString().trim().length <= 3 -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_NEW_ENTER_MORE_THAN_FOUR_DIGIT_PASSWORD,
                    getString(R.string.new_password_can_not_less_then_4_blank)
                )
            }
            confirmPasswordEET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_CONFIRM_PASSWORD, getString(R.string.enter_confirm_password))

            }
            confirmPasswordEET.text.toString().trim().length <= 3 -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_CONFIRM_ENTER_MORE_THAN_FOUR_DIGIT_PASSWORD,
                    getString(R.string.confirm_password_can_not_less_then_4_blank)
                )
            }
            confirmPasswordEET.text.toString() != newPasswordEET.text.toString() -> {
                msg = LabelUtils.getLabel(this, NEW_PASSWORD_AND_REPEAT_PASSWORD_NOT_MATCH,getString(R.string.confirm_password_can_not_match))
            }

        }
        return msg
    }

    override fun onClick(v: View?) {

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.currentPasswordEET -> Validation.TansformPassword(event, currentPasswordEET)
            R.id.newPasswordEET -> Validation.TansformPassword(event, newPasswordEET)
            R.id.confirmPasswordEET -> Validation.TansformPassword(event, confirmPasswordEET)
        }
        return false
    }
    private fun callChangePasswordApi() {
        val map: HashMap<String, Any> = HashMap()
        map[PARAM_CURRENT_PASSWORD] = currentPasswordEET.text.toString().trim()
        map[PARAM_NEW_PASSWORD] = newPasswordEET.text.toString().trim()
        map[PARAM_CONFIRM_PASSWORD] = confirmPasswordEET.text.toString().trim()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_DEVICE_TYPE] = "1"

        changePasswordViewModel.callCreatePasswordApi(map)
    }

    private fun handleApi() {
        changePasswordViewModel.changePasswordResponseLiveData.observe(this, androidx.lifecycle.Observer {
            when (it[0].code) {
                SUCCESS -> {
                    toast(it[0].message)
                    onBackPressed()

                }
                FAILURE -> {
                    toast(it[0].message)
                }

            }
        })
    }
}
