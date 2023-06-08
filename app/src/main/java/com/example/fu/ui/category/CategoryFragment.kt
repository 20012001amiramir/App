package com.example.fu.ui.category

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentCategoryBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.model.Disease
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.diseaseListDelegate
import com.example.fu.ui.gridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch

class CategoryFragment: Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)

    private val args: CategoryFragmentArgs by navArgs()

    private val categoryViewModel:
            CategoryViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val diseaseListDelegate = ListDelegationAdapter(
        diseaseListDelegate(
            onClick = { disease -> navigateToDiseaseDetail(disease) },
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel.loadData(args.categoryId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            rvDiseases.apply {
                layoutManager = gridLayoutManager(2)
                adapter = diseaseListDelegate
            }

            topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.uiState.collect { state -> handleUiState(state) }
            }
        }
    }

    private fun handleUiState(uiState: CategoryUiState) {
        when(uiState) {
            is CategoryUiState.Idle -> Unit
            is CategoryUiState.Empty -> {
                with(binding) {
                    topAppBar.apply {
                        title = uiState.category.plantName
                        subtitle = uiState.category.countOfDiseases.toString()
                    }
                }
            }
            is CategoryUiState.Success -> {
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())

                with(binding) {
                    topAppBar.apply {
                        title = uiState.category.plantName
                        subtitle = resources.getString(
                            R.string.subtitle_count_of_diseases,
                            uiState.category.countOfDiseases.toString()
                        )
                    }
                }

                diseaseListDelegate.swapItems(uiState.diseases)
            }
        }
    }


}

private fun FragmentCategoryBinding.applyInsets() {
    apply {
        appBarLayout.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                )
            }
            windowInsets
        }
    }
}

private fun CategoryFragment.navigateToDiseaseDetail(disease: Disease) {
    val directions = CategoryFragmentDirections
        .actionCategoryFragmentToDiseaseDetailFragment(diseaseId = disease.id)
    findNavController().navigate(directions)
}
