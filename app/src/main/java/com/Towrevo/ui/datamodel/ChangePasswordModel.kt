package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class ChangePasswordModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Any>,
    @SerializedName("message")
    var message: String
)