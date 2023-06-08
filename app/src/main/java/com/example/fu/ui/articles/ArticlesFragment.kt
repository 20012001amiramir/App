package com.example.fu.ui.articles

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.NavigationArticlesDirections
import com.example.fu.NavigationHomeDirections
import com.example.fu.R
import com.example.fu.databinding.FragmentArticleBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.model.Article
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.verticalLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch

class ArticlesFragment : Fragment(R.layout.fragment_article) {

    private val articlesViewModel:
            ArticlesViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)


    private val binding by viewBinding(FragmentArticleBinding::bind)

    private val articlesDelegate = ListDelegationAdapter(
        articlesListDelegate(
            onClick = { article -> navigateToArticleDetail(article) }
        )
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            rvArticles.apply {
                adapter = articlesDelegate
                layoutManager = verticalLayoutManager()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                articlesViewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
    }

    private fun handleUiState(uiState: ArticlesUiState) {
        when(uiState) {
            is ArticlesUiState.Idle -> Unit
            is ArticlesUiState.Empty -> {
                binding.containerPlaceholder.visibility = View.VISIBLE
            }
            is ArticlesUiState.Success -> {
                binding.containerPlaceholder.visibility = View.GONE

                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                articlesDelegate.swapItems(uiState.articles)
            }
        }
    }

}

private fun ArticlesFragment.navigateToArticleDetail(article: Article) {
    val directions = NavigationArticlesDirections
        .actionNavigationArticlesArticleDetailFragment(articleId = article.id)
    findNavController().navigate(directions)
}

private fun FragmentArticleBinding.applyInsets() {
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