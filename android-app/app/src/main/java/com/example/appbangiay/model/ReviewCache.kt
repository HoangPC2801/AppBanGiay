package com.example.appbangiay.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bang_review_cache")
data class ReviewCache(
    @PrimaryKey
    val id: Int,

    val productId: Int,
    val firebaseUid: String?,
    val userName: String?,
    val rating: Int,
    val comment: String?,
    val reviewImage: String?,
    val adminReply: String?,
    val createdAt: String?,
    val likeCount: Int
)