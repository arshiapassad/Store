package com.example.store.ui.profile_favorite

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.store.R
import com.example.store.databinding.FragmentProfileFavoriteBinding
import com.example.store.utils.base.BaseFragment
import com.example.store.utils.extensions.showSnackBar
import com.example.store.viewmodel.ProfileFavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.data.models.profile_favorite.ResponseProfileFavorites
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.utils.network.NetworkRequest

@AndroidEntryPoint
class ProfileFavoriteFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentProfileFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    //Other
    private val viewModel by viewModels<ProfileFavoritesViewModel>()
    private var recyclerviewState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable)
            viewModel.callFavoritesApi()
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                toolbarTitleTxt.text = getString(R.string.yourFavorites)
                toolbarOptionImg.isVisible = false
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
            }
        }
        //Load data
        loadFavoritesData()
        loadDeleteFavoriteData()
    }

    private fun loadFavoritesData() {
        binding.apply {
            viewModel.favoritesData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        favoritesList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        favoritesList.hideShimmer()
                        response.data?.let { data ->
                            if (data.data.isNotEmpty()) {
                                initRecycler(data.data)
                            } else {
                                emptyLay.isVisible = true
                                favoritesList.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        favoritesList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun loadDeleteFavoriteData() {
        binding.apply {
            viewModel.deleteFavoriteData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let {
                            if (isNetworkAvailable)
                                viewModel.callFavoritesApi()
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<ResponseProfileFavorites.Data>) {
        binding.apply {
            favoritesAdapter.setData(data)
            favoritesList.setupRecyclerview(LinearLayoutManager(requireContext()), favoritesAdapter)
            //Auto scroll
            favoritesList.layoutManager?.onRestoreInstanceState(recyclerviewState)
            //Click
            favoritesAdapter.setOnItemClickListener {
                //Save state
                recyclerviewState = favoritesList.layoutManager?.onSaveInstanceState()
                //Call delete api
                if (isNetworkAvailable)
                    viewModel.callDeleteFavoriteApi(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}