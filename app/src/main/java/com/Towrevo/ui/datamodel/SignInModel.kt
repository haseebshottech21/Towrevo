package com.Towrevo.ui.datamodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class SignInModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("about")
        val about: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("category")
        val category: List<Category>,
        @SerializedName("company_name")
        val companyName: String,
        @SerializedName("company_profile_image")
        val companyProfileImage: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("is_verify")
        val isVerify: String,
        @SerializedName("last_name")
        val lastName: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("otp")
        val otp: String,
        @SerializedName("profile_image")
        val profileImage: String,
        @SerializedName("token")
        val token: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("user_type")
        val userType: String,
        @SerializedName("working_hour")
        val workingHour: String
    ) {
        data class Category(
            @SerializedName("id")
            val id: Int,
            @SerializedName("name")
            val name: String
        )
    }
}