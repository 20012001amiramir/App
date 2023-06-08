package com.example.fu.ui.article_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.data.repository.NewsRepository
import com.example.fu.model.Article
import com.example.fu.model.Disease
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _uiState:
            MutableStateFlow<ArticleDetailUiState> = MutableStateFlow(ArticleDetailUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun fetchArticle(id: Int) {
        viewModelScope.launch {
            val article = newsRepository.getArticleById(id)
            _uiState.update { ArticleDetailUiState.Success(article) }
        }
    }

}

sealed class ArticleDetailUiState {

    data class Success(val article: Article): ArticleDetailUiState()

    object Idle: ArticleDetailUiState()

}