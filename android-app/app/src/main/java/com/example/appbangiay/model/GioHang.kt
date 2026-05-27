package com.example.appbangiay.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bang_gio_hang")
data class GioHang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firebaseUid: String = "",

    val maGiay: Int,
    val tenGiay: String,
    val giaTien: Float,
    val hinhAnh: String?,

    val mauSac: String? = null,
    val size: String? = null,

    val soLuong: Int = 1
) : Parcelable