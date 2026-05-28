package com.example.appbangiay.model

data class TinNhanChat(
    val id: String = "",
    val text: String = "",
    val sender: String = "",
    val senderName: String = "",
    val createdAt: Long = 0L,
    val seen: Boolean = false
)