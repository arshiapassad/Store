package com.example.store.data.repository

import com.example.store.data.models.address.BodySetAddressForShipping
import com.example.store.data.models.shipping.BodyCoupon
import com.example.store.data.network.ApiServices
import javax.inject.Inject

class ShippingRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getShipping() = api.getShipping()
    suspend fun putSetAddress(body: BodySetAddressForShipping) = api.putSetAddress(body)
    suspend fun postCoupon(body: BodyCoupon) = api.postCoupon(body)
    suspend fun postPayment(body: BodyCoupon) = api.postPayment(body)
}