package com.example.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.models.profile_order.ResponseProfileOrdersList
import com.example.store.data.repository.ProfileOrdersRepository
import com.example.store.utils.network.NetworkRequest
import com.example.store.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileOrdersViewModel @Inject constructor(private val repository: ProfileOrdersRepository) : ViewModel() {

    //Orders list
    private val _ordersData = MutableLiveData<NetworkRequest<ResponseProfileOrdersList>>()
    val ordersData: LiveData<NetworkRequest<ResponseProfileOrdersList>> = _ordersData

    fun callOrdersApi(status: String) = viewModelScope.launch {
        _ordersData.value = NetworkRequest.Loading()
        val response = repository.getProfileOrdersList(status)
        _ordersData.value = NetworkResponse(response).generateResponse()
    }
}