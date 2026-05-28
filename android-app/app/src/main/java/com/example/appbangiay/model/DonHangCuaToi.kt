package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class DonHangCuaToi(
    val id: Int,
    val total: Float,
    val status: String,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("product_name")
    val productName: String?,

    @SerializedName("items_count")
    val itemsCount: Int = 0
) {
    val tenSanPhamHienThi: String
        get() = if (itemsCount > 1) {
            "${productName ?: "Sản phẩm"} và ${itemsCount - 1} sản phẩm khác"
        } else {
            productName ?: "Sản phẩm"
        }
}