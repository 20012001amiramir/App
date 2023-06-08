package com.example.fu.ui.article_detail

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
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.fu.R
import com.example.fu.databinding.FragmentArticleDetailBinding
import com.example.fu.databinding.FragmentDiseaseDetailBinding
import com.example.fu.databinding.ItemCarouselBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.ui.carouselLayoutManager
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.photosListDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import kotlinx.coroutines.launch

class ArticleDetailFragment: Fragment(R.layout.fragment_article_detail) {

    private val articleDetailViewModel:
            ArticleDetailViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val binding by viewBinding(FragmentArticleDetailBinding::bind)

    private val args: ArticleDetailFragmentArgs by navArgs()

    private val photosListDelegate = ListDelegationAdapter(
        photosListDelegate(
            onClick = { url ->  }
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleDetailViewModel.fetchArticle(args.articleId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }

            rvPhotos.adapter = photosListDelegate
            rvPhotos.layoutManager = carouselLayoutManager()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleDetailViewModel.uiState.collect { state -> handleUiState(state) }
            }
        }
    }

    private fun handleUiState(uiState: ArticleDetailUiState) {
        when(uiState) {
            is ArticleDetailUiState.Success -> {
                with(binding) {

                    tvContent.text = uiState.article.content
                    tvTitle.text = uiState.article.title

                    photosListDelegate.swapItems(uiState.article.urlToImages)
                }
            }
            is ArticleDetailUiState.Idle -> Unit
        }
    }

}

private fun FragmentArticleDetailBinding.applyInsets() {
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

