package com.example.store.ui.search_filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.models.search.ResponseSearch.Products.Data
import com.example.store.data.models.search_filter.FilterModel
import com.example.store.databinding.FragmentSearchFilterBinding
import com.example.store.utils.extensions.setupRecyclerview
import com.example.store.viewmodel.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFilterFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentSearchFilterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var filterAdapter: FilterAdapter

    //Theme
    override fun getTheme() = R.style.RemoveDialogBackground

    //Other
    private val viewModel by activityViewModels<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            //Close
            closeImg.setOnClickListener { this@SearchFilterFragment.dismiss() }
        }
        //Fill filter data
        viewModel.getFilterData()
        //Load data
        loadFilterData()
    }

    private fun loadFilterData() {
        viewModel.filterData.observe(this) {
            initFilterRecycler(it)
        }
    }

    private fun initFilterRecycler(data: List<FilterModel>) {
        filterAdapter.setData(data)
        binding.filtersList.setupRecyclerview(LinearLayoutManager(requireContext()), filterAdapter)
        //Click
        filterAdapter.setOnItemClickListener {
            viewModel.sendSelectedFilterItem(it)
            this.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}