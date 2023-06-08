package com.example.fu.ui.enter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.persistent.LocalStorage
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterViewModel @Inject constructor(
    private val localStorage: LocalStorage
): ViewModel() {

    val isOnBoardingFinished = localStorage.isOnboardingFinished

    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch {
            localStorage.isOnboardingFinished = completed
        }
    }

}