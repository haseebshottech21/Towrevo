package com.Towrevo.ui.datamodel
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class CmsDataModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String
) {
    @Keep
    data class Data(
        @SerializedName("description")
        var description: String,
        @SerializedName("title")
        var title: String
    )
}