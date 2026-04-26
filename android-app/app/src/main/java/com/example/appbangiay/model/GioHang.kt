package com.example.appbangiay.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bang_gio_hang")
data class GioHang(
    @PrimaryKey val maGiay: Int,
    val tenGiay: String,
    val giaTien: Float,
    val hinhAnh: String?,
    var soLuong: Int
)