package com.example.appbangiay.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.appbangiay.model.SanPhamYeuThich
import kotlinx.coroutines.flow.Flow

@Dao
interface YeuThichDao {

    @Query("""
        SELECT * FROM bang_yeu_thich
        WHERE firebaseUid = :firebaseUid
        ORDER BY id DESC
    """)
    fun layDanhSachYeuThich(
        firebaseUid: String
    ): Flow<List<SanPhamYeuThich>>

    @Insert
    suspend fun themYeuThich(
        item: SanPhamYeuThich
    )

    @Delete
    suspend fun xoaYeuThich(
        item: SanPhamYeuThich
    )

    @Query("""
        SELECT * FROM bang_yeu_thich
        WHERE firebaseUid = :firebaseUid
        AND maGiay = :maGiay
        LIMIT 1
    """)
    suspend fun kiemTraDaYeuThich(
        firebaseUid: String,
        maGiay: Int
    ): SanPhamYeuThich?

    @Query("""
        DELETE FROM bang_yeu_thich
        WHERE firebaseUid = :firebaseUid
        AND maGiay = :maGiay
    """)
    suspend fun xoaTheoNguoiDungVaMaGiay(
        firebaseUid: String,
        maGiay: Int
    )

    @Query("""
    UPDATE bang_yeu_thich
    SET tenGiay = :tenGiay,
        giaTien = :giaTien,
        hinhAnh = :hinhAnh,
        giaGoc = :giaGoc,
        phanTramGiam = :phanTramGiam,
        averageRating = :averageRating,
        soldCount = :soldCount
    WHERE maGiay = :maGiay
    AND firebaseUid = :firebaseUid
    """)
    suspend fun capNhatThongTinYeuThich(
        firebaseUid: String,
        maGiay: Int,
        tenGiay: String,
        giaTien: Float,
        hinhAnh: String?,
        giaGoc: Float,
        phanTramGiam: Int,
        averageRating: Double?,
        soldCount: Int?
    )
}