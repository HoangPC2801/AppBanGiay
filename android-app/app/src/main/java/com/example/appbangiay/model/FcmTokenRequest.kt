package com.example.appbangiay.model

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("firebase_uid")
    val firebaseUid: String,

    val token: String
)