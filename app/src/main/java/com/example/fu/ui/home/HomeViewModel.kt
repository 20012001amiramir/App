package com.example.fu.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.repository.CategoryRepository
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.data.repository.NewsRepository
import com.example.fu.model.Article
import com.example.fu.model.Category
import com.example.fu.model.Disease
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val diseaseRepository: DiseaseRepository,
    private val newsRepository: NewsRepository,
): ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadHomeScreenInfo().collect { state -> _uiState.update { state } }
        }
    }

    private fun loadHomeScreenInfo() = flow {
        emit(HomeUiState.Loading)

        val categories = categoryRepository.getCategories()
        val disease = diseaseRepository.getFrequentDisease()
        val articles = newsRepository.getHeadlineArticles()

        emit(
            HomeUiState.Success(
                categories = categories,
                articles = articles,
                disease = disease
            )
        )
    }
        .catch { e -> emit(HomeUiState.Error(e.message.toString())) }

}

sealed class HomeUiState {

    data class Success(
        val disease: List<Disease>,
        val articles: List<Article>,
        val categories: List<Category>
    ): HomeUiState()

    object Loading: HomeUiState()

    data class Error(val message: String): HomeUiState()

    object Idle: HomeUiState()

}
