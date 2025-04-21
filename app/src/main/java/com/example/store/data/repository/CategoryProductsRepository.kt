package com.example.store.data.repository

import com.example.store.data.network.ApiServices
import javax.inject.Inject

class CategoryProductsRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProductsList(slug: String, queries: Map<String, String>) = api.getProductsList(slug,queries)
}