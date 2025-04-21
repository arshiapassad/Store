package com.example.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.models.profile_comments.ResponseDeleteComment
import com.example.store.data.models.profile_comments.ResponseProfileComments
import com.example.store.data.repository.ProfileCommentsRepository
import com.example.store.utils.network.NetworkRequest
import com.example.store.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileCommentsViewModel @Inject constructor(private val repository: ProfileCommentsRepository) : ViewModel() {
    //Comments list
    private val _commentsData = MutableLiveData<NetworkRequest<ResponseProfileComments>>()
    val commentsData: LiveData<NetworkRequest<ResponseProfileComments>> = _commentsData

    fun callCommentsApi() = viewModelScope.launch {
        _commentsData.value = NetworkRequest.Loading()
        val response = repository.getProfileComments()
        _commentsData.value = NetworkResponse(response).generateResponse()
    }

    //Delete comment
    private val _deleteCommentData = MutableLiveData<NetworkRequest<ResponseDeleteComment>>()
    val deleteCommentData: LiveData<NetworkRequest<ResponseDeleteComment>> = _deleteCommentData

    fun callDeleteCommentApi(id: Int) = viewModelScope.launch {
        _deleteCommentData.value = NetworkRequest.Loading()
        val response = repository.deleteProfileComment(id)
        _deleteCommentData.value = NetworkResponse(response).generateResponse()
    }
}