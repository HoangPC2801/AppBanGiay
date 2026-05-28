package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class AppBanner(
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    val title: String?,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("display_order")
    val displayOrder: Int
)