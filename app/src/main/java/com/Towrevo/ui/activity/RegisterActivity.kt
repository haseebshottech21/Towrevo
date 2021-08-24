package com.Towrevo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.util.extension.openActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.titlebar_layout.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()

    }

    private fun setLabel() {
    titleBarTV.text = LabelUtils.getLabel(this, REGISTER,R.string.register)
        registerTV.text = LabelUtils.getLabel(this, REGISTER_AS,R.string.register_as)
        companyTV.text = LabelUtils.getLabel(this, COMPANY,R.string.company)
        userTV.text = LabelUtils.getLabel(this, USER,R.string.user)

    }

    private fun setData() {
        companyCV.setOnClickListener(this)
        userCV.setOnClickListener(this)
        backIV.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIV -> finish()

            R.id.companyCV -> {
                companyCV.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
                companyTV.setTextColor(ContextCompat.getColor(this,R.color.blue))
                userTV.setTextColor(ContextCompat.getColor(this,R.color.Description))
                companyImageIV.setImageResource(R.drawable.ic_company1)
                userImageIV.setImageResource(R.drawable.ic_user1)
                userCV. setCardBackgroundColor (ContextCompat.getColor(this, R.color.white))
                openActivity(CompanyRegisterationActivity::class.java)

            }
            R.id.userCV->{
                companyCV.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                userCV. setCardBackgroundColor (ContextCompat.getColor(this, R.color.blue))
                companyTV.setTextColor(ContextCompat.getColor(this,R.color.Description))
                userImageIV.setImageResource(R.drawable.ic_user)
                companyImageIV.setImageResource(R.drawable.ic_company)
                userTV.setTextColor(ContextCompat.getColor(this,R.color.blue))
                openActivity(CustomerRegisterActivity::class.java)
            }

        }

    }
}