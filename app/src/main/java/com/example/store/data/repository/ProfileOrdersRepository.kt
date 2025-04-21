package com.example.store.data.repository

import com.example.store.data.network.ApiServices
import javax.inject.Inject

class ProfileOrdersRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileOrdersList(status: String) = api.getProfileOrdersList(status)
}