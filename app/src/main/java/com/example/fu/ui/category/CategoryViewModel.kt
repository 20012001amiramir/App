package com.example.fu.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.repository.CategoryRepository
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.model.Category
import com.example.fu.model.Disease
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    private val diseaseRepository: DiseaseRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _category = MutableStateFlow(Category(-1, "", 0, ""))
    private val _diseases = MutableStateFlow(listOf<Disease>())

    val uiState = combine(
            _category,
            _diseases
        ) { category, diseases ->
            if (diseases.isEmpty()) {
                CategoryUiState.Empty(category = category)
            } else {
                CategoryUiState.Success(category = category, diseases = diseases)
            }
        }
        .stateIn(
            initialValue = CategoryUiState.Idle,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    fun loadData(categoryId: Int) {
        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(categoryId)
            val diseases = diseaseRepository.getDiseaseByPlantName(category.plantName)

            _category.update { category }
            _diseases.update { diseases }
        }
    }

}

sealed class CategoryUiState {

    data class Success(
        val diseases: List<Disease>,
        val category: Category
    ): CategoryUiState()

    data class Empty(val category: Category): CategoryUiState()

    object Idle: CategoryUiState()

}