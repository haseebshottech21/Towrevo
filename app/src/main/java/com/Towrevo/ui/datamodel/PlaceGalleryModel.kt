package com.Towrevo.ui.datamodel


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceGalleryModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("total_count")
    var totalCount: Int
) : Serializable {
    data class Data(
        @SerializedName("id")
        var id: String,
        @SerializedName("place_id")
        var placeId: String,
        @SerializedName("posted_date")
        var postedDate: String,
        @SerializedName("poster_user_id")
        var posterUserId: String,
        @SerializedName("poster_username")
        var posterUsername: String,
        @SerializedName("url")
        var url: String
    ) : Serializable
}