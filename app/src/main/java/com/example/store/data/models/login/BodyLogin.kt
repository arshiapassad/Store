package com.example.store.data.models.login

import com.google.gson.annotations.SerializedName

data class BodyLogin(
    @SerializedName("login")
    var login: String? = null, // 09120174757
    @SerializedName("hash_code")
    var hashCode: String? = null, // dfUYjdf84
    @SerializedName("code")
    var code: Int? = null // 8456
)