package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class ResendModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String
)