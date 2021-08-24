package com.Towrevo.ui.datamodel


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CategoryDataModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String
) {
    @Keep
    data class Data(
        @SerializedName("category_id")
        var categoryId: String,
        @SerializedName("category_name")
        var categoryName: String,
        var isChecked: Boolean =false


    )
}