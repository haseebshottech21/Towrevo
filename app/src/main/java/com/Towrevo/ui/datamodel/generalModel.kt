package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class generalModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String
) {
    @Keep
    data class Data(
        @SerializedName("Applestore Link")
        var applestoreLink: String,
        @SerializedName("copyright")
        var copyright: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("facebook link")
        var facebookLink: String,
        @SerializedName("Instagram link")
        var instagramLink: String,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("Playstore Link")
        var playstoreLink: String,
        @SerializedName("twitter link")
        var twitterLink: String,
        @SerializedName("Website Logo")
        var websiteLogo: String,
        @SerializedName("Website name")
        var websiteName: String
    )
}