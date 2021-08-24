package com.Towrevo.ui.authentication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.ui.activity.SignInActivity
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CreatePasswordViewModel
import com.Towrevo.util.FAILURE
import com.Towrevo.util.SUCCESS
import com.Towrevo.util.Validation.TansformPassword
import com.Towrevo.util.extension.openActivityAndFinish
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_create_new_password.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import kotlinx.android.synthetic.main.titlebar_layout.*

class CreateNewPasswordActivity : AppCompatActivity() , View.OnClickListener,
View.OnTouchListener{

    private val createPasswordViewModel: CreatePasswordViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { CreatePasswordViewModel(this, mainRL, noInternetView) }).get(
            CreatePasswordViewModel::class.java
        )
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.resetBTN ->{
                val msg=isValidDetail()
                if(msg.isEmpty()) {
                    //call api
                    callCreatePasswordApi()
                }else {
                    toast(msg)
                }}
            R.id.retryTV->{}
            R.id.backIV->finish()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.newPasswordEET -> {
                TansformPassword(event, newPasswordEET)
            }
            R.id.confirmPasswordEET -> {
                TansformPassword(event, confirmPasswordEET)
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_password)
        setLabel()
        setData()
        handleAPI()
        titleBarTV.visibility = View.GONE
    }

    private fun handleAPI() {
        createPasswordViewModel.createPasswordResponse.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {

                    openActivityAndFinish(SignInActivity::class.java)
                    toast(it[0].message)
                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }

        })
    }

    private fun callCreatePasswordApi() {

        val map: HashMap<String, Any> = HashMap()

        map[PARAM_NEW_PASSWORD] = newPasswordEET.text.toString().trim()
        map[PARAM_CONFIRM_PASSWORD] = confirmPasswordEET.text.toString().trim()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_DEVICE_TYPE] = "1"
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
        createPasswordViewModel.callCreatePasswordApi(map)

    }

    private fun setData() {
        resetBTN.setOnClickListener(this)
        retryTV.setOnClickListener(this)
        backIV.setOnClickListener(this)
        newPasswordEET.setOnTouchListener(this)
        confirmPasswordEET.setOnTouchListener(this)

    }

    private fun setLabel() {
        setTitleBar()
        retryTV.text=LabelUtils.getLabel(this, RETRY, R.string.retry)
        createTextTV.text=LabelUtils.getLabel(this, CREATE_NEW_PASSWORD, R.string.create_new_password)
        newPasswordTV.text= LabelUtils.getLabel(this, NEW_PASSWORD, R.string.new_password)
        confirmPasswordTV.text= LabelUtils.getLabel(this, CONFIRM_PASSWORD, R.string.confirm_password)
        resetBTN.text = LabelUtils.getLabel(this, RESET, R.string.reset)
    }

    private fun setTitleBar() {
        /* title bar*/
//        titleBarTV.text= LabelUtils.getLabel(this, CHANGE_PASSWORD, R.string.change_password)
        searchIV.visibility=GONE
        backIV.visibility=VISIBLE
        backIV.setOnClickListener(this)
        /**********************/
    }
    /**
     * check validation
     */
    private fun isValidDetail(): String {

        var msg = ""

        when {

            newPasswordEET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_NEW_PASSWORD,getString(R.string.new_pw_can_not_blank))

            }
            confirmPasswordEET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_CONFIRM_PASSWORD,getString(R.string.confirm_pw_can_not_blank))

            }
            newPasswordEET.text.toString() != confirmPasswordEET.text.toString() -> {
                msg = LabelUtils.getLabel(this, CONFIRM_PW_NOT_MATCH,getString(R.string.confirm_pw_can_not_match))
            }
        }
        return msg
    }
}
