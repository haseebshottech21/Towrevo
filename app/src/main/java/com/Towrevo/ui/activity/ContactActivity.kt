package com.Towrevo.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.preference.PreferenceHelper
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.titlebar_layout.*

class ContactActivity : AppCompatActivity(), View.OnClickListener {
    var mobileNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        titleBarTV.visibility = View.VISIBLE
    }

    private fun setLabel() {
        titleBarTV.text = LabelUtils.getLabel(this, CONTACT_US, R.string.contact_us)
//        callID.text = LabelUtils.getLabel(this, CALL_ID, R.string.contact_id)
//        mailID.text = LabelUtils.getLabel(this, MAIL_ID, R.string.mail_id)
        callUsTV.text = LabelUtils.getLabel(this, CALL_US, R.string.call_us)
        mailUsTV.text = LabelUtils.getLabel(this, MAIL_US, R.string.mail_us)
    }

    private fun setData() {
        callID.setOnClickListener(this)
        mailLL.setOnClickListener(this)
        backIV.setOnClickListener(this)
        mobileNumber = PreferenceHelper.getSettingMobile()
        callID.text = PreferenceHelper.getSettingMobile()
        mailID.text = PreferenceHelper.getSettingEmail()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.callID -> {
                val intent = Intent()
                intent.action = Intent.ACTION_DIAL // Action for what intent called for
                intent.data =
                    Uri.parse("tel: $mobileNumber") // Data with intent respective action on intent
                startActivity(intent)
            }
            R.id.mailLL -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(PreferenceHelper.getSettingEmail())
                }
                startActivity(Intent.createChooser(emailIntent, "Send feedback"))
            }
            R.id.backIV -> {
                finish()
            }
        }
    }
}