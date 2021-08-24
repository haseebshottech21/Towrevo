package com.Towrevo.ui.datamodel

import com.google.gson.annotations.SerializedName



data class FaqModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<List<Data>>,
    @SerializedName("message")
    var message: String
) {
    data class Data(
        @SerializedName("Answer")
        var answer: String,
        @SerializedName("Question")
        var question: String
    )
}