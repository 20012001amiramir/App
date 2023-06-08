package com.example.fu.ui.disease_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.model.Disease
import com.example.fu.util.fromJson
import com.example.fu.util.toJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiseaseDetailViewModel @Inject constructor(
    private val diseaseRepository: DiseaseRepository,
    private val localStorage: LocalStorage,
): ViewModel() {

    private val _uiState:
            MutableStateFlow<DiseaseDetailUiState> = MutableStateFlow(DiseaseDetailUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val bookmarksIds = localStorage.bookmarkIds
        .map { json -> json?.fromJson<List<Int>>() ?: listOf() }
        .stateIn(
            started = SharingStarted.Eagerly,
            initialValue = listOf(),
            scope = viewModelScope
        )

    private val _isInBookmarks = MutableStateFlow(false)
    val isInBookmark = _isInBookmarks.asStateFlow()

    fun fetchDisease(id: Int) {
        viewModelScope.launch {
            val disease = diseaseRepository.getDiseaseById(id)
            _uiState.update { DiseaseDetailUiState.Success(disease) }

            _isInBookmarks.update { bookmarksIds.value.contains(disease.id) }
        }
    }

    fun bookmarkToggle(diseaseId: Int) {
        viewModelScope.launch {
            if (_isInBookmarks.value) {
                val json = bookmarksIds.value.toMutableList()
                    .apply { remove(diseaseId) }
                    .toJson()

                localStorage.dataBookmarkDiseaseIds = json
                _isInBookmarks.update { false }
            } else {
                val json = bookmarksIds.value.toMutableList()
                    .apply { add(diseaseId) }
                    .toJson()

                localStorage.dataBookmarkDiseaseIds = json
                _isInBookmarks.update { true }
            }
        }
    }

}

sealed class DiseaseDetailUiState {

    data class Success(val disease: Disease): DiseaseDetailUiState()

    object Idle: DiseaseDetailUiState()

}