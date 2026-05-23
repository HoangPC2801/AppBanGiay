package com.example.appbangiay.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bang_yeu_thich")
data class SanPhamYeuThich(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firebaseUid: String,

    val maGiay: Int,
    val tenGiay: String,
    val giaTien: Float,
    val hinhAnh: String?
)