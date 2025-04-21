package com.example.store.data.repository


import com.example.store.data.network.ApiServices
import javax.inject.Inject

class ProfileFavoritesRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileFavorites() = api.getProfileFavorites()
    suspend fun deleteProfileFavorite(id: Int) = api.deleteProfileFavorite(id)
}