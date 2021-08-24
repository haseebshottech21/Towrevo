package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class UserRegisterModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String
) {
    @Keep
    data class Data(
        @SerializedName("about")
        var about: String,
        @SerializedName("address")
        var address: String,
        @SerializedName("company_name")
        var companyName: String,
        @SerializedName("company_profile_image")
        var companyProfileImage: String,
        @SerializedName("country_code")
        var countryCode: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("first_name")
        var firstName: String,
        @SerializedName("is_verify")
        var isVerify: String,
        @SerializedName("last_name")
        var lastName: String,
        @SerializedName("latitude")
        var latitude: String,
        @SerializedName("longitude")
        var longitude: String,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("otp")
        var otp: String,
        @SerializedName("profile_image")
        var profileImage: String,
        @SerializedName("token")
        var token: String,
        @SerializedName("user_id")
        var userId: String,
        @SerializedName("user_type")
        var userType: String,
        @SerializedName("working_hour")
        var workingHour: String
    )
}