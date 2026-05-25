package com.example.appbangiay.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appbangiay.model.ReviewCache
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewCacheDao {

    @Query("SELECT * FROM bang_review_cache WHERE productId = :productId ORDER BY id DESC")
    fun layReviewCache(productId: Int): Flow<List<ReviewCache>>

    @Query("DELETE FROM bang_review_cache WHERE productId = :productId")
    suspend fun xoaReviewTheoSanPham(productId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun themDanhSachReview(reviews: List<ReviewCache>)
}