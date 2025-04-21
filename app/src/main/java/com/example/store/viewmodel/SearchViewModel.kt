package com.example.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.models.search.ResponseSearch
import com.example.store.data.models.search_filter.FilterModel
import com.example.store.data.repository.SearchFilterRepository
import com.example.store.data.repository.SearchRepository
import com.example.store.utils.Q
import com.example.store.utils.SORT
import com.example.store.utils.network.NetworkRequest
import com.example.store.utils.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository, private val filterRepository: SearchFilterRepository
) : ViewModel() {
    //Search
    fun searchQueries(search: String, sort: String): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Q] = search
        queries[SORT] = sort
        return queries
    }

    private val _searchData = MutableLiveData<NetworkRequest<ResponseSearch>>()
    val searchData: LiveData<NetworkRequest<ResponseSearch>> = _searchData

    fun callSearchApi(queries: Map<String, String>) = viewModelScope.launch {
        _searchData.value = NetworkRequest.Loading()
        val response = repository.getSearchList(queries)
        _searchData.value = NetworkResponse(response).generateResponse()
    }

    //Filter
    private val _filterData = MutableLiveData<MutableList<FilterModel>>()
    val filterData: LiveData<MutableList<FilterModel>> = _filterData

    fun getFilterData() = viewModelScope.launch {
        _filterData.value = filterRepository.fillFilterData()
    }

    //Filter selected
    private val _filterSelectedItem = MutableLiveData<String>()
    val filterSelectedItem: LiveData<String> = _filterSelectedItem

    fun sendSelectedFilterItem(item: String) {
        _filterSelectedItem.value = item
    }
}