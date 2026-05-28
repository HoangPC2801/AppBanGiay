package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class ThongBao(
    val id: Int,

    @SerializedName("firebase_uid")
    val firebaseUid: String?,

    val title: String,

    val message: String,

    val type: String,

    @SerializedName("related_order_id")
    val relatedOrderId: Int?,

    @SerializedName("is_read")
    val isRead: Boolean,

    @SerializedName("created_at")
    val createdAt: String?
)