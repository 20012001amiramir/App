package com.example.fu.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.NavigationSearchDirections
import com.example.fu.R
import com.example.fu.databinding.FragmentSearchBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.model.Disease
import com.example.fu.ui.common.extention.clear
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.diseaseListDelegate
import com.example.fu.ui.gridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch

class SearchFragment: Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val searchViewModel:
            SearchViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)


    private val searchListDelegate = ListDelegationAdapter(
        diseaseListDelegate(
            onClick = { disease -> navigateToDiseaseDetail(disease)}
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.uiState.collect { uiState -> handleUiState(uiState) }
            }
        }
    }

    private fun handleUiState(uiState: SearchUiState) {
        when(uiState) {
            is SearchUiState.Idle -> Unit
            is SearchUiState.Empty -> {
                binding.containerPlaceholder.visibility = View.VISIBLE
                searchListDelegate.clear()
            }
            is SearchUiState.Success -> {
                binding.containerPlaceholder.visibility = View.GONE
                searchListDelegate.swapItems(uiState.items)
            }
        }
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()
            rvDiseaseSearch.apply {
                adapter = searchListDelegate
                layoutManager = gridLayoutManager(2)
                etSearch.addTextChangedListener { editable ->
                    searchViewModel.enterSearch(editable.toString())
                }
            }
            etSearch.addTextChangedListener { editable ->
                searchViewModel.enterSearch(editable.toString())
            }
        }
    }

}

private fun FragmentSearchBinding.applyInsets() {
    apply {
        appBarLayout.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                )
            }
            windowInsets
        }
    }
}

private fun SearchFragment.navigateToDiseaseDetail(disease: Disease) {
    val directions = NavigationSearchDirections
        .actionNavigationSearchToDiseaseDetailFragment(diseaseId = disease.id)
    findNavController().navigate(directions)
}