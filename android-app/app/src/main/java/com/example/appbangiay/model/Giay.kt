package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class Giay(
    @SerializedName("id") val maGiay: Int,
    @SerializedName("name") val tenGiay: String,
    @SerializedName("price") val giaTien: Float,
    @SerializedName("description") val moTa: String?,
    @SerializedName("image") val hinhAnh: String?,
    @SerializedName("category_id") val maDanhMuc: Int
)