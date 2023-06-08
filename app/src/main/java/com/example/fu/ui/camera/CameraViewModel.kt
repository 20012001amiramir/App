package com.example.fu.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.data.repository.ClassificationResult
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.data.repository.ModelRepository
import com.example.fu.model.Disease
import com.example.fu.util.fromJson
import com.example.fu.util.toJson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    private val diseaseRepository: DiseaseRepository,
    private val localStorage: LocalStorage,
    private val modelRepository: ModelRepository
): ViewModel() {

    private val _uiState: MutableSharedFlow<CameraUiState> = MutableSharedFlow()
    val uiState = _uiState.asSharedFlow()

    private val search = MutableStateFlow("")

    private val bookmarksIds = localStorage.bookmarkIds
        .map { json -> json?.fromJson<List<Int>>() ?: listOf() }
        .stateIn(
            started = SharingStarted.Eagerly,
            initialValue = listOf(),
            scope = viewModelScope
        )

    val searchItems = combine(
            search,
            diseaseRepository.getDiseasesFlow()
        ) { search, disease ->
            if (search.isEmpty()) {
                listOf()
            } else {
                disease.filter { disease ->
                    disease.diseaseName.contains(search) ||
                            disease.description.contains(search) ||
                            disease.plantName.contains(search) ||
                            disease.danger.contains(search) ||
                            disease.type.contains(search) ||
                            disease.treatment.contains(search)
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf()
            )

    fun classify(image: File) {
        viewModelScope.launch {
            _uiState.emit(CameraUiState.Loading)

            val result = modelRepository.classifyDisease(image)

            when(result) {
                is ClassificationResult.Healthy -> {
                    _uiState.emit(CameraUiState.Healthy)
                }
                is ClassificationResult.Error -> {
                    _uiState.emit(CameraUiState.Error(result.message))
                }
                is ClassificationResult.Success -> {
                    _uiState.emit(CameraUiState.Success(result.disease))
                }
            }
        }
    }

    fun saveBookmark(disease: Disease) {
        viewModelScope.launch {
            val json = bookmarksIds.value.toMutableList().apply {
                add(disease.id)
            }.toJson()
            localStorage.dataBookmarkDiseaseIds = json
        }
    }

    fun removeBookmark(disease: Disease) {
        viewModelScope.launch {
            val json = bookmarksIds.value.toMutableList().apply {
                remove(disease.id)
            }.toJson()
            localStorage.dataBookmarkDiseaseIds = json
        }
    }

    fun enterSearch(s: String) {
        search.update { s.lowercase() }
    }

    fun isInBookmarks(disease: Disease) = bookmarksIds.value.contains(disease.id)

}



sealed class CameraUiState {

    object Loading: CameraUiState()

    data class Error(val error: String): CameraUiState()

    data class Success(val disease: Disease): CameraUiState()

    object Healthy: CameraUiState()

    object NothingWasFound: CameraUiState()

    object Idle: CameraUiState()

}