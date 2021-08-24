package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Keep
data class NearByCompanyModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("page")
    var page: String,
    @SerializedName("total_count")
    var totalCount: Int
):Serializable {
    @Keep
    data class Data(
        @SerializedName("company_details")
        var companyDetails: String,
        @SerializedName("company_name")
        var companyName: String,
        @SerializedName("company_profile_image")
        var companyProfileImage: String,
        @SerializedName("distance")
        var distance: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("id")
        var id: String,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("place")
        var place: Place,
        @SerializedName("working_hours")
        var workingHours: String
    ):Serializable {
        @Keep
        data class Place(
            @SerializedName("address")
            var address: String,
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("longitude")
            var longitude: String
        ):Serializable

        constructor() : this(
            "",
            "",
            "",
            "","","","",Place("","",""),"")

    }
}