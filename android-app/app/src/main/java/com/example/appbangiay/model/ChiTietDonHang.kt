package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class ChiTietDonHang(
    val id: Int,
    val total: Float,
    val status: String,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("shipping_address")
    val shippingAddress: String?,

    @SerializedName("customer_name")
    val customerName: String?,

    @SerializedName("customer_email")
    val customerEmail: String?,

    @SerializedName("payment_method")
    val paymentMethod: String?,

    val items: List<ChiTietSanPhamDonHang>
)

data class ChiTietSanPhamDonHang(
    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("product_name")
    val productName: String?,

    @SerializedName("product_image")
    val productImage: String?,

    val quantity: Int,
    val price: Float,
    val color: String?,
    val size: String?
)