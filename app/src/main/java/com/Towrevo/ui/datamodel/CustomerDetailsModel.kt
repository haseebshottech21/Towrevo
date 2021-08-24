package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Keep
data class CustomerDetailsModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String
): Serializable {
    @Keep
    data class Data(
        @SerializedName("about")
        var about: String,
        @SerializedName("date")
        var date: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("image")
        var image: List<Image>,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("place")
        var place: Place,
        @SerializedName("profile_image")
        var profileImage: String,
        @SerializedName("user_id")
        var userId: String,
        @SerializedName("user_name")
        var userName: String
    ):Serializable {
        @Keep
        data class Image(
            @SerializedName("id")
            var id: Int,
            @SerializedName("image")
            var image: String
        ):Serializable

        @Keep
        data class Place(
            @SerializedName("address")
            var address: String,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("longitude")
            var longitude: String
        ):Serializable
    }
}