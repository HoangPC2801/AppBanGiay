package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class ProductReviewCreate(

    @SerializedName("firebase_uid")
    val firebaseUid: String,

    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("review_image")
    val reviewImage: String? = null
)

data class ProductReviewOut(

    val id: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("firebase_uid")
    val firebaseUid: String,

    @SerializedName("user_name")
    val userName: String?,

    val rating: Int,

    val comment: String?,

    @SerializedName("review_image")
    val reviewImage: String?,

    @SerializedName("is_hidden")
    val isHidden: Boolean = false,

    @SerializedName("admin_reply")
    val adminReply: String?,

    @SerializedName("admin_reply_at")
    val adminReplyAt: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    @SerializedName("like_count")
    val likeCount: Int = 0
)

data class ProductReviewSummary(

    @SerializedName("average_rating")
    val averageRating: Float,

    @SerializedName("review_count")
    val reviewCount: Int,

    @SerializedName("sold_count")
    val soldCount: Int,

    val page: Int,

    val limit: Int,

    val reviews: List<ProductReviewOut>
)