package com.example.fu.ui.profile_edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.repository.ProfileRepository
import com.example.fu.model.Profile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {

    val uiState = profileRepository.profile
        .map { profile ->
            ProfileEditUiState.Success(profile)
        }
        .stateIn(
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileEditUiState.Idle,
            scope = viewModelScope
        )

    fun saveProfileName(profileName: String) {
        viewModelScope.launch {
            profileRepository.updateName(profileName)
        }
    }

    fun saveProfileImage(uri: Uri) {
        viewModelScope.launch {
            profileRepository.updateImage(uri)
        }
    }

}

sealed class ProfileEditUiState {

    data class Success(val profile: Profile): ProfileEditUiState()

    object Idle: ProfileEditUiState()

}