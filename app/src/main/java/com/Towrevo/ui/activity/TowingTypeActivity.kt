package com.Towrevo.ui.activity

import android.graphics.Color
import android.widget.ArrayAdapter
import com.Towrevo.R
import com.twoSecure.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_towing_type.*
import kotlinx.android.synthetic.main.activity_towing_type.vehicleTypeSpinner
import kotlinx.android.synthetic.main.toolbar_layout.*

class TowingTypeActivity : BaseActivity() {


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_towing_type
    }

    override fun main() {
       setLable()
       setData()
       onClick()
    }

    private fun onClick() {
        image_back_arrow.setOnClickListener {
            onBackPressed()
        }
        nowBTN.setOnClickListener {
            nowBTN.setTextColor(Color.parseColor("#125570"))
            nowBTN.setBackgroundResource(R.drawable.button_background)

            scheduleBTN.setTextColor(Color.parseColor("#FFFFFF"))
            scheduleBTN.setBackgroundResource(R.drawable.bg_round_corner_border)
        }
        scheduleBTN.setOnClickListener {
            scheduleBTN.setTextColor(Color.parseColor("#125570"))
            scheduleBTN.setBackgroundResource(R.drawable.button_background)

            nowBTN.setTextColor(Color.parseColor("#FFFFFF"))
            nowBTN.setBackgroundResource(R.drawable.bg_round_corner_border)
        }
    }

    private fun setData() {
        setSpinnerData()
    }

    private fun setLable() {

    }
    fun setSpinnerData(){
        var vList_type = arrayListOf<String>()
        vList_type.add("Type 1")
        vList_type.add("Type 2")
        vList_type.add("Type 3")
        vList_type.add("Type 4")
        vList_type.add("Type 5")

        val vTypeAdpter = ArrayAdapter(this,android.R.layout.simple_list_item_1,vList_type)
        vehicleTypeSpinner.adapter = vTypeAdpter

    }
}