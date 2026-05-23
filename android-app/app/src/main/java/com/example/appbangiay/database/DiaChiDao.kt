package com.example.appbangiay.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.appbangiay.model.DiaChi
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaChiDao {

    @Query("SELECT * FROM bang_dia_chi WHERE firebaseUid = :firebaseUid ORDER BY id DESC")
    fun layTheoNguoiDung(firebaseUid: String): Flow<List<DiaChi>>

    @Insert
    suspend fun themDiaChi(diaChi: DiaChi)

    @Delete
    suspend fun xoaDiaChi(diaChi: DiaChi)
}