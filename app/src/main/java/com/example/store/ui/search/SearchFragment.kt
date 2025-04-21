package com.example.store.ui.search

import academy.nouri.storeapp.ui.search.adapters.SearchAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.store.R
import com.example.store.data.models.home.ResponseDiscount.ResponseDiscountItem
import com.example.store.data.models.search.ResponseSearch
import com.example.store.data.models.search.ResponseSearch.Products
import com.example.store.data.models.search.ResponseSearch.Products.*
import com.example.store.databinding.FragmentSearchBinding
import com.example.store.utils.NEW
import com.example.store.utils.base.BaseFragment
import com.example.store.utils.extensions.loadImage
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.utils.extensions.showKeyboard
import com.example.store.utils.extensions.showSnackBar
import com.example.store.utils.network.NetworkRequest
import com.example.store.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var searchAdapter: SearchAdapter

    //Other
    private val viewModel by activityViewModels<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Toolbar
            toolbar.apply {
                //Back
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                //Title
                toolbarTitleTxt.text = getString(R.string.searchInProducts)
                //Option
                toolbarOptionImg.isVisible = false
            }
            //Navigate to filter
            filterImg.setOnClickListener {
                findNavController().navigate(R.id.actionSearchToFilter)
            }
            //Auto open keyboard
            lifecycleScope.launch {
                delay(300)
                searchEdt.showKeyboard(requireActivity())
            }
            //Search
            searchEdt.addTextChangedListener {
                if (it.toString().length > 3) {
                    if (isNetworkAvailable) {
                        viewModel.callSearchApi(viewModel.searchQueries(it.toString(), NEW))
                    }
                }
                //Empty
                if (it.toString().isEmpty()) {
                    emptyLay.isVisible = true
                    searchList.isVisible = false
                }
            }
            //Update sort
            viewModel.filterSelectedItem.observe(viewLifecycleOwner) {
                if (searchEdt.toString().length > 3) {
                    if (isNetworkAvailable)
                        viewModel.callSearchApi(viewModel.searchQueries(searchEdt.text.toString(), it))
                }
            }
        }
        //Load data
        loadSearchData()
    }

    private fun loadSearchData() {
        binding.apply {
            viewModel.searchData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        searchList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        searchList.hideShimmer()
                        response.data?.let { data ->
                            data.products?.let { products ->
                                if (products.data?.isNotEmpty()!!) {
                                    emptyLay.isVisible = false
                                    searchList.isVisible = true
                                    //Init recycler
                                    initSearchRecycler(products.data)
                                } else {
                                    emptyLay.isVisible = true
                                    searchList.isVisible = false
                                }
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        searchList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initSearchRecycler(data: List<Data>) {
        searchAdapter.setData(data)
        binding.searchList.setupRecyclerview(LinearLayoutManager(requireContext()), searchAdapter)
        //Click
        searchAdapter.setOnItemClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}