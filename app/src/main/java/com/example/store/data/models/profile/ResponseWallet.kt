package com.example.store.data.models.profile


import com.google.gson.annotations.SerializedName

data class ResponseWallet(
    @SerializedName("wallet")
    val wallet: String? // 450000
)