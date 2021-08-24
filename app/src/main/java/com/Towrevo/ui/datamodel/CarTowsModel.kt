package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class CarTowsModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String
) {
    @Keep
    data class Data(
        @SerializedName("company_name")
        var companyName: String,
        @SerializedName("company_profile_image")
        var companyProfileImage: String,
        @SerializedName("description")
        var description: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("image")
        var image: List<Image>,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("user_id")
        var userId: String
    ) {
        @Keep
        data class Image(
            @SerializedName("id")
            var id: Int,
            @SerializedName("image")
            var image: String
        )
    }
}