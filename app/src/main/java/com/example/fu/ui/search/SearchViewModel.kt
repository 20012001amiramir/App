package com.example.fu.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.model.Disease
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    diseaseRepository: DiseaseRepository
): ViewModel() {

    private val search = MutableStateFlow("")

    val uiState = combine(
            search,
            diseaseRepository.getDiseasesFlow()
        ) { search, diseases ->
            if (search.isEmpty()) {
                SearchUiState.Success(diseases)
            } else {
                val filteredDiseases = diseases.filter { disease ->
                    disease.diseaseName.contains(search) ||
                            disease.description.contains(search) ||
                            disease.plantName.contains(search) ||
                            disease.danger.contains(search) ||
                            disease.type.contains(search) ||
                            disease.treatment.contains(search)
                }
                if (filteredDiseases.isEmpty()) {
                    SearchUiState.Empty
                } else{
                    SearchUiState.Success(filteredDiseases)
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchUiState.Idle
            )

    fun enterSearch(s: String) {
        search.update { s.lowercase() }
    }

}

sealed class SearchUiState {

    object Idle: SearchUiState()

    object Empty: SearchUiState()

    data class Success(val items: List<Disease>): SearchUiState()

}