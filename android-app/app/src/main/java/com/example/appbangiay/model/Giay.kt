package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class Giay(
    @SerializedName("id") val maGiay: Int,
    @SerializedName("name") val tenGiay: String,
    @SerializedName("price") val giaTien: Float,
    @SerializedName("description") val moTa: String?,
    @SerializedName("image") val hinhAnh: String?,
    @SerializedName("category_id") val maDanhMuc: Int?,
    @SerializedName("category") val danhMuc: String? = null,
    @SerializedName("brand") val thuongHieu: String? = null,
    @SerializedName("variants") val variants: List<BienTheGiay> = emptyList()

)

data class BienTheGiay(
    @SerializedName("id") val id: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("color") val mauSac: String?,
    @SerializedName("size") val size: String?,
    @SerializedName("stock_quantity") val soLuongTon: Int
)