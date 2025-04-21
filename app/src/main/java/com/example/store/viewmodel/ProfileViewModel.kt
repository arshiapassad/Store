package com.example.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.models.login.BodyLogin
import com.example.store.data.models.login.ResponseLogin
import com.example.store.data.models.profile.BodyUpdateProfile
import com.example.store.data.models.profile.ResponseProfile
import com.example.store.data.repository.ProfileRepository
import com.example.store.utils.network.NetworkRequest
import com.example.store.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            delay(300)
            callProfileApi()
        }
    }

    //Profile
    private val _profileData = MutableLiveData<NetworkRequest<ResponseProfile>>()
    val profileData: LiveData<NetworkRequest<ResponseProfile>> = _profileData

    fun callProfileApi() = viewModelScope.launch {
        _profileData.value = NetworkRequest.Loading()
        val response = repository.getProfileData()
        _profileData.value = NetworkResponse(response).generateResponse()
    }

    //Upload Avatar
    private val _avatarData = MutableLiveData<NetworkRequest<Unit>>()
    val avatarData: LiveData<NetworkRequest<Unit>> = _avatarData

    fun callUploadAvatarApi(body: RequestBody) = viewModelScope.launch {
        _avatarData.value = NetworkRequest.Loading()
        val response = repository.postUploadAvatar(body)
        _avatarData.value = NetworkResponse(response).generateResponse()
    }

    //Profile
    private val _updateProfileData = MutableLiveData<NetworkRequest<ResponseProfile>>()
    val updateProfileData: LiveData<NetworkRequest<ResponseProfile>> = _updateProfileData

    fun callUpdateProfileApi(body: BodyUpdateProfile) = viewModelScope.launch {
        _updateProfileData.value = NetworkRequest.Loading()
        val response = repository.postUploadProfile(body)
        _updateProfileData.value = NetworkResponse(response).generateResponse()
    }
}