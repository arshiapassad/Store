package com.example.store.ui.profile_comments

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.models.profile_comments.ResponseProfileComments
import com.example.store.databinding.FragmentProfileCommentsBinding
import com.example.store.utils.base.BaseFragment
import com.example.store.utils.extensions.isVisible
import com.example.store.utils.extensions.moneySeparating
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.utils.extensions.showSnackBar
import com.example.store.utils.network.NetworkRequest
import com.example.store.viewmodel.ProfileCommentsViewModel
import com.example.store.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileCommentsFragment : BaseFragment() {
    //Binding
    private var _binding: FragmentProfileCommentsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var commentsAdapter: CommentsAdapter

    //Other
    private val viewModel by viewModels<ProfileCommentsViewModel>()
    private var recyclerviewState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileCommentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Call api
        if (isNetworkAvailable)
            viewModel.callCommentsApi()
        //InitViews
        binding.apply {
            //Toolbar
            toolbar.apply {
                toolbarTitleTxt.text = getString(R.string.yourComments)
                toolbarOptionImg.isVisible = false
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
            }
        }
        //Load data
        loadCommentsData()
        loadDeleteCommentData()
    }

    private fun loadCommentsData() {
        binding.apply {
            viewModel.commentsData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        commentsList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        commentsList.hideShimmer()
                        response.data?.let { data ->
                            if (data.data.isNotEmpty()) {
                                initCommentsRecycler(data.data)
                            } else {
                                emptyLay.isVisible = true
                                commentsList.isVisible = false
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        commentsList.hideShimmer()
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun loadDeleteCommentData() {
        binding.apply {
            viewModel.deleteCommentData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let {
                            if (isNetworkAvailable)
                                viewModel.callCommentsApi()
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initCommentsRecycler(data: List<ResponseProfileComments.Data>) {
        binding.apply {
            commentsAdapter.setData(data)
            commentsList.setupRecyclerview(LinearLayoutManager(requireContext()), commentsAdapter)
            //Auto scroll
            commentsList.layoutManager?.onRestoreInstanceState(recyclerviewState)
            //Click
            commentsAdapter.setOnItemClickListener {
                //Save state
                recyclerviewState = commentsList.layoutManager?.onSaveInstanceState()
                //Call delete api
                if (isNetworkAvailable)
                    viewModel.callDeleteCommentApi(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}