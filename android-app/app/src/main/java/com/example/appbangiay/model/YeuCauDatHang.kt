package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class YeuCauDatHang(
    @SerializedName("user_id") val maNguoiDung: Int,
    @SerializedName("total") val tongTien: Float,
    @SerializedName("shipping_address") val diaChiGiaoHang: String,
    @SerializedName("payment_method") val phuongThucThanhToan: String,
    @SerializedName("items") val danhSachMonHang: List<ChiTietMonHang>
)

data class ChiTietMonHang(
    @SerializedName("product_id") val maGiay: Int,
    @SerializedName("quantity") val soLuong: Int,
    @SerializedName("price") val giaTien: Float
)