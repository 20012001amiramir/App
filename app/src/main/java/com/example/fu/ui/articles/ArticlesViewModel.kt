package com.example.fu.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.repository.NewsRepository
import com.example.fu.model.Article
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ArticlesViewModel @Inject constructor(
    newsRepository: NewsRepository
): ViewModel() {

    val uiState = newsRepository.getHeadlineArticlesFlow()
        .map { articles ->
            if (articles.isEmpty()) {
                ArticlesUiState.Empty
            } else {
                ArticlesUiState.Success(articles)
            }
        }
        .stateIn(
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticlesUiState.Idle,
            scope = viewModelScope
        )

}

sealed class ArticlesUiState {

    object Idle: ArticlesUiState()

    object Empty: ArticlesUiState()

    data class Success(val articles: List<Article>): ArticlesUiState()

}