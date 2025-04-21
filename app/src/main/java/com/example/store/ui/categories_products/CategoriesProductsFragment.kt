package com.example.store.ui.categories_products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.models.home.ResponseProducts
import com.example.store.databinding.FragmentCategoriesProductsBinding
import com.example.store.utils.base.BaseFragment
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.utils.extensions.showSnackBar
import com.example.store.utils.network.NetworkRequest
import com.example.store.viewmodel.CategoryProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesProductsFragment : BaseFragment() {

    //Binding
    private var _binding: FragmentCategoriesProductsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var productsAdapter: ProductsAdapter

    //Other
    private val viewModel by activityViewModels<CategoryProductsViewModel>()
    private val args by navArgs<CategoriesProductsFragmentArgs>()
    private var slug = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            slug = it.slug
        }
        //Call api
        if (isNetworkAvailable)
            viewModel.callProductApi(slug, viewModel.productsQueries())
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                //Back
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                //Filter
                toolbarOptionImg.setOnClickListener { findNavController().navigate(R.id.actionCategoryToFilter) }
            }
        }
        //Load data
        loadProductsData()
        //Update data
        receiveCategoryFilter()
    }

    private fun loadProductsData() {
        binding.apply {
            viewModel.productData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        productsList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        productsList.hideShimmer()
                        response.data?.let { data ->
                            //Title
                            binding.toolbar.toolbarTitleTxt.text = data.subCategory?.title
                            //RecyclerView
                            data.subCategory?.products?.let { products ->
                                if (products.data!!.isNotEmpty()) {
                                    emptyLay.isVisible = false
                                    productsList.isVisible = true
                                    //Init recycler
                                    initProductsRecycler(products.data)
                                } else {
                                    emptyLay.isVisible = true
                                    productsList.isVisible = false
                                }
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        productsList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initProductsRecycler(data: List<ResponseProducts.SubCategory.Products.Data>) {
        productsAdapter.setData(data)
        binding.productsList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            productsAdapter
        )
        //Click
        productsAdapter.setOnItemClickListener {

        }
    }

    private fun receiveCategoryFilter() {
        viewModel.filterCategoryData.observe(viewLifecycleOwner) {
            if (isNetworkAvailable)
                viewModel.callProductApi(
                    slug,
                    viewModel.productsQueries(
                        sort = it.sort,
                        search = it.search,
                        minPrice = it.minPrice,
                        maxPrice = it.maxPrice,
                        available = it.available
                    )
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}