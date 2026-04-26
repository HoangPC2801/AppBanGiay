package com.example.appbangiay.database

import androidx.room.*
import com.example.appbangiay.model.GioHang
import kotlinx.coroutines.flow.Flow

@Dao
interface GioHangDao {
    @Query("SELECT * FROM bang_gio_hang")
    fun layDanhSachGioHang(): Flow<List<GioHang>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun themVaoGio(monHang: GioHang)

    @Delete
    suspend fun xoaKhoiGio(monHang: GioHang)

    @Query("DELETE FROM bang_gio_hang")
    suspend fun xoaToanBoGioHang()
}