package com.example.fu.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.data.repository.ProfileRepository
import com.example.fu.model.Profile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    profileRepository: ProfileRepository
): ViewModel() {

    val uiState = profileRepository.profile
        .map { profile ->
            ProfileUiState.Success(profile)
        }
        .stateIn(
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Idle,
            scope = viewModelScope
        )

}

sealed class ProfileUiState {

    data class Success(val profile: Profile): ProfileUiState()

    object Idle: ProfileUiState()

}