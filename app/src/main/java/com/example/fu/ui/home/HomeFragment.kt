package com.example.fu.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.NavigationHomeDirections
import com.example.fu.R
import com.example.fu.databinding.FragmentHomeBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.model.Article
import com.example.fu.model.Category
import com.example.fu.model.Disease
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.gridLayoutManager
import com.example.fu.ui.horizontalLayoutManager
import com.example.fu.ui.verticalLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {

    private val homeViewModel:
            HomeViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val frequentDiseaseListDelegate = ListDelegationAdapter(
        frequentDiseaseListDelegate(
            onClick = { disease -> navigateToDiseaseDetail(disease) }
        )
    )
    private val newsListDelegate = ListDelegationAdapter(
        newsListDelegate(
            onClick = { article -> navigateToArticleDetail(article) }
        )
    )
    private val categoriesListDelegate = ListDelegationAdapter(
        categoriesListDelegate(
            onClick = { category -> navigateToCategory(category) }
        )
    )

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        setupUi()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { state -> handleUiState(state) }
            }
        }
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            btnScanDisease.setOnClickListener(this@HomeFragment)

            rvFrequentIllnesses.apply {
                layoutManager = horizontalLayoutManager()
                adapter = frequentDiseaseListDelegate
            }
            rvNews.apply {
                layoutManager = verticalLayoutManager()
                adapter = newsListDelegate
            }
            rvCategories.apply {
                layoutManager = gridLayoutManager(2)
                adapter = categoriesListDelegate
            }
        }
    }

    private fun handleUiState(uiState: HomeUiState) {
        when(uiState) {
            is HomeUiState.Success -> {
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())

                categoriesListDelegate.swapItems(uiState.categories)
                frequentDiseaseListDelegate.swapItems(uiState.disease)
                newsListDelegate.swapItems(uiState.articles)

                setProgressBarVisibility(false)
            }
            is HomeUiState.Error -> {
                Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_LONG).show()
                setProgressBarVisibility(false)
            }
            is HomeUiState.Loading -> {
                setProgressBarVisibility(true)
            }
            is HomeUiState.Idle -> Unit
        }
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.progressBar.visibility = visibility
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnScanDisease -> navigateToCamera()
        }
    }

}

private fun FragmentHomeBinding.applyInsets() {
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

private fun HomeFragment.navigateToArticleDetail(article: Article) {
    val directions = NavigationHomeDirections
        .actionNavigationHomeToArticleDetailFragment(articleId = article.id)
    findNavController().navigate(directions)
}

private fun HomeFragment.navigateToDiseaseDetail(disease: Disease) {
    val directions = NavigationHomeDirections
        .actionNavigationHomeToDiseaseDetailFragment(diseaseId = disease.id)
    findNavController().navigate(directions)
}

private fun HomeFragment.navigateToCategory(category: Category) {
    val directions = NavigationHomeDirections
        .actionNavigationHomeToCategoryFragment(categoryId = category.id)
    findNavController().navigate(directions)
}

private fun HomeFragment.navigateToCamera() {
    findNavController().navigate(R.id.action_navigationHome_to_cameraFragment)
}
