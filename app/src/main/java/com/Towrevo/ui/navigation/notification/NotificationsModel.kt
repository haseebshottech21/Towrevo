package com.vrin.taylorenggpms.ui.navigation.notification

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationsModel(
    val notification_id: String,
    val notification_name: String,
    val notification_date: String
) : Serializable