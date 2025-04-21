package com.example.store.data.repository

import com.example.store.data.network.ApiServices
import javax.inject.Inject

class ProfileCommentsRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileComments() = api.getProfileComments()
    suspend fun deleteProfileComment(id: Int) = api.deleteProfileComment(id)
}