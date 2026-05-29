package com.example.appbangiay.model

data class TinNhanChat(
    val id: String = "",
    val text: String = "",
    val sender: String = "",
    val senderName: String = "",
    val createdAt: Long = 0L,
    val seen: Boolean = false,

    val messageType: String = "text",
    val productId: Int? = null,
    val productName: String? = null,
    val productImage: String? = null,
    val productPrice: Int? = null
)