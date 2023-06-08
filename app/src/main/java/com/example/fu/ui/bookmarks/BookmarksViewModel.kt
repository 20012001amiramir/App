package com.example.fu.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.model.Disease
import com.example.fu.util.fromJson
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class BookmarksViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val diseaseRepository: DiseaseRepository
): ViewModel() {

    private val bookmarksIds = localStorage.bookmarkIds
        .map { json -> json?.fromJson<List<Int>>() ?: listOf() }
        .stateIn(
            started = SharingStarted.Eagerly,
            initialValue = listOf(),
            scope = viewModelScope
        )

    val uiState: StateFlow<BookmarksUiState> = bookmarksIds
        .map { ids ->
            val diseases = ids.map { id -> diseaseRepository.getDiseaseById(id) }

            if (diseases.isEmpty()) {
                BookmarksUiState.Empty
            } else {
                BookmarksUiState.Success(diseases)
            }
        }
        .stateIn(
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BookmarksUiState.Idle,
            scope = viewModelScope
        )
}

sealed class BookmarksUiState {

    data class Success(val bookmarks: List<Disease>): BookmarksUiState()

    object Empty: BookmarksUiState()

    object Idle: BookmarksUiState()

}