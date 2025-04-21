package com.example.store.data.repository

import com.example.store.data.network.ApiServices
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getCategoriesList() = api.getCategoriesList()
}