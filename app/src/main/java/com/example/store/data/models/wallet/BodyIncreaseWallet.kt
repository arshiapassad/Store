package com.example.store.data.models.wallet

import com.google.gson.annotations.SerializedName

data class BodyIncreaseWallet(
    @SerializedName("amount")
    var amount: String? = null // 40000
)