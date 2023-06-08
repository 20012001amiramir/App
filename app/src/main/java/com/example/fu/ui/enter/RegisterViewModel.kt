package com.example.fu.ui.enter

import android.content.ActivityNotFoundException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.network.response.RegisterResponse
import com.example.fu.interactor.SessionInteractors
import com.example.fu.util.ErrorHandler
import kotlinx.coroutines.launch
import com.example.fu.interactor.AccountInteractor
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val sessionInteractors: SessionInteractors,
    private val errorHandler: ErrorHandler,
    private val accountInteractor: AccountInteractor
) : ViewModel() {

    private val _loginViewState: MutableLiveData<LoginViewState> = MutableLiveData(LoginViewState.BlankViewState)
    val loginViewState: LiveData<LoginViewState> get() = _loginViewState

    fun register(email: String, password: String, passwordRepeat: String) {
        _loginViewState.value = LoginViewState.LoadingViewState
        viewModelScope.launch {
            try {
                _loginViewState.postValue(
                    LoginViewState.Data(sessionInteractors.register(email, password, passwordRepeat))
                )
            } catch (e: ActivityNotFoundException) {
                errorHandler.proceed(e)
            }
        }
    }

    sealed class LoginViewState {
        data class Data(val data: RegisterResponse) : LoginViewState()
        object LoadingViewState : LoginViewState()
        object BlankViewState : LoginViewState()
    }
}