package com.example.fu.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.NavigationBookmarksDirections
import com.example.fu.NavigationSearchDirections
import com.example.fu.R
import com.example.fu.databinding.FragmentBookmarksBinding
import com.example.fu.databinding.FragmentHomeBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.model.Disease
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.diseaseListDelegate
import com.example.fu.ui.gridLayoutManager
import com.example.fu.ui.home.newsListDelegate
import com.example.fu.ui.search.SearchFragment
import com.example.fu.ui.verticalLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch

class BookmarksFragment: Fragment(R.layout.fragment_bookmarks) {

    private val binding by viewBinding(FragmentBookmarksBinding::bind)

    private val bookmarksViewModel:
            BookmarksViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val bookmarksListDelegate = ListDelegationAdapter(
        diseaseListDelegate(
            onClick = { disease -> navigateToDiseaseDetail(disease) },
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun handleUiState(uiState: BookmarksUiState) {
        when(uiState) {
            is BookmarksUiState.Idle -> Unit
            is BookmarksUiState.Empty -> {
                binding.containerPlaceholder.visibility = View.VISIBLE
            }
            is BookmarksUiState.Success -> {
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())

                binding.containerPlaceholder.visibility = View.GONE
                bookmarksListDelegate.swapItems(uiState.bookmarks)
            }
        }
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            rvBookmarks.apply {
                adapter = bookmarksListDelegate
                layoutManager = gridLayoutManager(2)
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarksViewModel.uiState.collect { state -> handleUiState(state) }
            }
        }
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.progressBar.visibility = visibility
    }

}

private fun FragmentBookmarksBinding.applyInsets() {
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

private fun BookmarksFragment.navigateToDiseaseDetail(disease: Disease) {
    val directions = NavigationBookmarksDirections
        .actionNavigationBookmarksToDiseaseDetailFragment(diseaseId = disease.id)
    findNavController().navigate(directions)
}
