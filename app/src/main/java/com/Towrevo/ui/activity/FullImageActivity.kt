package com.Towrevo.ui.activity

import android.view.View
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.util.*
import com.Towrevo.util.extension.gone
import com.Towrevo.util.extension.visible
import com.twoSecure.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_full_image.*
import kotlinx.android.synthetic.main.titlebar_layout.*


class FullImageActivity : BaseActivity(), View.OnClickListener {

    private var imageUrl = ""
    override fun getLayoutResourceId(): Int = R.layout.activity_full_image

    override fun main() {
        backIV.gone()
        titleBarTV.gone()
        setData()
        closeIv.visible()
        if (intent.hasExtra(IMAGE_DETAIL_CONTAINER)) {
            imageUrl = intent.getStringExtra(IMAGE_DETAIL_CONTAINER) as String
            Glide.with(this).load(imageUrl).into(photo_view)
        }


    }

    private fun setData() {
        closeIv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closeIv -> {
                onBackPressed()
            }
        }
    }


}
