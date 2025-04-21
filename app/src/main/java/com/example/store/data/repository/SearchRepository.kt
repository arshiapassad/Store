package com.example.store.data.repository

import com.example.store.data.network.ApiServices
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getSearchList(queries: Map<String, String>) = api.getSearchList(queries)
}