package com.Towrevo.ui.activity

import android.view.View
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.util.extension.openActivity
import com.twoSecure.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_recepient_details.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class RecepientDetailsActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_recepient_details
    }

    override fun main() {
       setTitle()
       setLable()
       setData()
       onClick()
    }

    private fun setTitle() {
        signUpLable.visibility = View.VISIBLE
        signUpLable.text = LabelUtils.getLabel(this, RECEPIENT_DETAILS,getString(R.string.recepient_details))
    }

    private fun onClick() {
        image_back_arrow.setOnClickListener {
            onBackPressed()
        }
        continueBTN.setOnClickListener {
            val msg = isValidDetail()
           /* if (msg.isEmpty()){
                //api call
            }
            else{
                toast(msg)
            }*/
            openActivity(AddVehicleActivity::class.java)
        }
    }

    private fun setData() {

    }

    private fun setLable() {

        rNameLT.hint = LabelUtils.getLabel(this,ENTER_RECEPIENT_NAME,R.string.enter_recepient_name)
        rPhoneNumberLT.hint = LabelUtils.getLabel(this,ENTER_RECEPIENT_PHONE_NUMBER,R.string.enter_recepient_pnumber)
        chnageTV.text = LabelUtils.getLabel(this,CHANGE,R.string.change)
        continueBTN.text = LabelUtils.getLabel(this,CONTINUE,R.string.continue_key)

    }
    private fun isValidDetail(): String {
        var msg=""

        when {
            rNameET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_RECEPIENT_NAME, getString(R.string.please_enter_recepient_name))

            }
            rPhoneNumberET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_ENTER_RECEPIENT_PHONE_NUMBER, getString(R.string.please_enter_recepient_pnumber))

            }

        }
        return msg
    }
}