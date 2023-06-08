package com.example.fu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.interactor.SessionInteractors
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AppActivityViewModel @Inject constructor(
    private val sessionInteractors: SessionInteractors,
    ): ViewModel() {


    private val _appViewState = MutableLiveData<AppViewState>()
    val appViewState get() = _appViewState

    init {
        viewModelScope.launch {
            navigateToMainScreen()
        }
    }
    private fun navigateToMainScreen() {
            if (sessionInteractors.isAuthorized()) {
                _appViewState.value = AppViewState.QrReader
            } else {
                _appViewState.value = AppViewState.Login
            }
    }


    fun notifyScheduleShown() {
        _appViewState.value = AppViewState.CurrentFragmentState
    }

    fun setQrReader(){
        _appViewState.value = AppViewState.QrReader
    }

    sealed class AppViewState {
        object Register : AppViewState()
        object Login : AppViewState()
        object QrReader : AppViewState()
        object Schedule : AppViewState()
        object CurrentFragmentState : AppViewState()
        object BlockingLoading : AppViewState()
        object ForceUpdate : AppViewState()
    }
}