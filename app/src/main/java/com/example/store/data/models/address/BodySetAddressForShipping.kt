package com.example.store.data.models.address

import com.google.gson.annotations.SerializedName

data class BodySetAddressForShipping(
    @SerializedName("addressId")
    var addressId: String? = null // 7
)