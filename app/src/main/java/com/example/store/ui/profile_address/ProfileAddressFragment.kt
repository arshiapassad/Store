package com.example.store.ui.profile_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.models.address.ResponseProfileAddresses.ResponseProfileAddressesItem
import com.example.store.databinding.FragmentProfileAddressBinding
import com.example.store.databinding.FragmentProfileBinding
import com.example.store.utils.base.BaseFragment
import com.example.store.utils.events.EventBus
import com.example.store.utils.events.Events
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.utils.extensions.showSnackBar
import com.example.store.utils.network.NetworkRequest
import com.example.store.viewmodel.ProfileAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileAddressFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentProfileAddressBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var addressesAdapter: AddressesAdapter

    //Other
    private val viewModel by viewModels<ProfileAddressViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable)
            viewModel.callAddressesListApi()
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                toolbarTitleTxt.text = getString(R.string.yourAddresses)
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                //Add
                toolbarOptionImg.apply {
                    setImageResource(R.drawable.location_plus)
                    setOnClickListener {
                        findNavController().navigate(R.id.actionProfileToAddressAdd)
                    }
                }
            }
        }
        //Auto update
        lifecycleScope.launch {
            EventBus.subscribe<Events.IsUpdateAddress> {
                if (isNetworkAvailable)
                    viewModel.callAddressesListApi()
            }
        }
        //Load data
        loadAddressesData()
    }

    private fun loadAddressesData() {
        binding.apply {
            viewModel.addressesListData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        addressList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        addressList.hideShimmer()
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                initRecycler(data)
                            } else {
                                emptyLay.isVisible = true
                                addressList.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        addressList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<ResponseProfileAddressesItem>) {
        binding.apply {
            addressesAdapter.setData(data)
            addressList.setupRecyclerview(LinearLayoutManager(requireContext()), addressesAdapter)
            //Click
            addressesAdapter.setOnItemClickListener {
                val direction = ProfileAddressFragmentDirections.actionProfileToAddressAdd().setData(it)
                findNavController().navigate(direction)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}