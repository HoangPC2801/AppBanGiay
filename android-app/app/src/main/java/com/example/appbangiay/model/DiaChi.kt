package com.example.appbangiay.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bang_dia_chi")
data class DiaChi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firebaseUid: String,

    val tenNguoiNhan: String,
    val tinhThanh: String,
    val diaChiChiTiet: String
)