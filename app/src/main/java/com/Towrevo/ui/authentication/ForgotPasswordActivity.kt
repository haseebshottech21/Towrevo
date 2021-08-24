package com.Towrevo.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.util.Validation.isEmailValid
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.emailET
import kotlinx.android.synthetic.main.activity_forgot_password.emailHintTV
import kotlinx.android.synthetic.main.no_internet_layout.*
import kotlinx.android.synthetic.main.titlebar_layout.*

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setLabel()
        setData()
    }
    private fun setData() {
        sendButton.setOnClickListener(this)
        retryTV.setOnClickListener(this)

    }

    private fun setLabel() {
        setTitleBar()
        retryTV.text=LabelUtils.getLabel(this, RETRY, R.string.retry)
        emailHintTV.hint = LabelUtils.getLabel(this, EMAIL, R.string.email)
        sendButton.text=LabelUtils.getLabel(this, SEND, R.string.send)
    }

    private fun setTitleBar() {
        /* title bar*/
        titleBarTV.text= LabelUtils.getLabel(this, FORGOT_PW, R.string.forget_password)
        searchIV.visibility= GONE
        backIV.visibility=VISIBLE
        backIV.setOnClickListener(this)
        /**********************/
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sendButton -> {

                val msg = isValidDetail()
                if (msg.isEmpty()) {
                   //call api
                } else {
                    toast(msg)
                }
            }
            R.id.retryTV -> {}
            R.id.backIV->finish()
        }
    }

    private fun isValidDetail(): String {
        var msg = ""

        when {
            emailET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_EMAIL,getString(R.string.email_can_not_blank))

            }
            !isEmailValid(emailET.text.toString()) -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_VALID_EMAIL,getString(R.string.email_should_valid))

            }
        }
        return msg
    }
}
