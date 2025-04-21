package com.example.store.data.repository

import com.example.store.data.models.login.BodyLogin
import com.example.store.data.network.ApiServices
import javax.inject.Inject

class LoginRepository @Inject constructor(private val api: ApiServices){
    suspend fun postLogin(body: BodyLogin) = api.postLogin(body)
    suspend fun postVerify(body: BodyLogin) = api.postVerify(body)
}