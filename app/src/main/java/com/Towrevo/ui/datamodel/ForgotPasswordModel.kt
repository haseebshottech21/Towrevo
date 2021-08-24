package com.Towrevo.ui.datamodel


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ForgotPasswordModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String
)