package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Keep
data class CompanyInquiryListModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("page")
    var page: Any
):Serializable {
    @Keep
    data class Data(
        @SerializedName("date")
        var date: String,
        @SerializedName("id")
        var id: String,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("profile_image")
        var profileImage: String,
        @SerializedName("user_name")
        var userName: String
    ):Serializable
}