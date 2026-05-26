package com.example.appbangiay.database

import androidx.room.*
import com.example.appbangiay.model.GioHang
import kotlinx.coroutines.flow.Flow

@Dao
interface GioHangDao {

    @Query("SELECT * FROM bang_gio_hang WHERE firebaseUid = :firebaseUid")
    fun layTheoNguoiDung(firebaseUid: String): Flow<List<GioHang>>

    @Query("DELETE FROM bang_gio_hang WHERE firebaseUid = :firebaseUid")
    suspend fun xoaTheoNguoiDung(firebaseUid: String)

    @Insert
    suspend fun themVaoGio(gioHang: GioHang)

    @Update
    suspend fun capNhat(gioHang: GioHang)

    @Delete
    suspend fun xoa(gioHang: GioHang)

    @Query("DELETE FROM bang_gio_hang")
    suspend fun xoaTatCa()

    @Query("""
    SELECT COALESCE(SUM(soLuong), 0)
    FROM bang_gio_hang
    WHERE firebaseUid = :firebaseUid
    """)
    fun layTongSoLuongGioHang(firebaseUid: String): Flow<Int>

    @Query("""
    UPDATE bang_gio_hang
    SET tenGiay = :tenGiay,
        giaTien = :giaTien,
        hinhAnh = :hinhAnh
    WHERE maGiay = :maGiay
    AND firebaseUid = :firebaseUid
    """)
    suspend fun capNhatThongTinSanPhamTrongGioHang(
        firebaseUid: String,
        maGiay: Int,
        tenGiay: String,
        giaTien: Float,
        hinhAnh: String?
    )
}