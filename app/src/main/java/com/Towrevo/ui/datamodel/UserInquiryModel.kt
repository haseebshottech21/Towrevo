package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class UserInquiryModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Any>,
    @SerializedName("message")
    var message: String
)