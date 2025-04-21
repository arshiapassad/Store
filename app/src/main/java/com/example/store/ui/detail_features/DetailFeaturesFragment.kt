package com.example.store.ui.detail_features

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.models.detail.ResponseProductFeatures
import com.example.store.databinding.FragmentDetailFeatureslBinding
import com.example.store.utils.base.BaseFragment
import com.example.store.utils.extensions.isVisible
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.utils.extensions.showSnackBar
import com.example.store.utils.network.NetworkRequest
import com.example.store.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFeaturesFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentDetailFeatureslBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var featuresAdapter: FeaturesAdapter

    private val viewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailFeatureslBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Get id
        viewModel.productID.observe(viewLifecycleOwner){
            if (isNetworkAvailable)
                viewModel.callProductFeatures(it)
        }
        //Load data
        loadFeaturesData()
    }
    private fun loadFeaturesData() {
        binding.apply {
            viewModel.featuresData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        featuresLoading.isVisible(true, featuresList)
                    }

                    is NetworkRequest.Success -> {
                        featuresLoading.isVisible(false, featuresList)
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                initRecycler(data)
                                emptyLay.isVisible = false
                            } else {
                                emptyLay.isVisible = true
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        featuresLoading.isVisible(false, featuresList)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<ResponseProductFeatures.ResponseProductFeaturesItem>) {
        featuresAdapter.setData(data)
        binding.featuresList.setupRecyclerview(LinearLayoutManager(requireContext()), featuresAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}