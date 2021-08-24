package com.Towrevo.ui.datamodel

import com.google.gson.annotations.SerializedName


data class LabelModel (
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("updated_date") val updated_date : String,
    @SerializedName("result") val result : List<HashMap<String,String>>
)