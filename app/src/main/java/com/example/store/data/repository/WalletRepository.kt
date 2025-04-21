package com.example.store.data.repository

import com.example.store.data.models.wallet.BodyIncreaseWallet
import com.example.store.data.network.ApiServices
import javax.inject.Inject

class WalletRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getWallet() = api.getWallet()
    suspend fun postIncreaseWallet(body: BodyIncreaseWallet) = api.postIncreaseWallet(body)
}