package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Keep
data class CustomerInquiryListModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("page")
    var page: Any
) : Serializable {
    @Keep
    data class Data(
        @SerializedName("address")
        var address: String,
        @SerializedName("company_name")
        var companyName: String,
        @SerializedName("date")
        var date: String,
        @SerializedName("id")
        var id: String,
        @SerializedName("latitude")
        var latitude: String,
        @SerializedName("longitude")
        var longitude: String,
        @SerializedName("profile_image")
        var profileImage: String
    ):Serializable
}