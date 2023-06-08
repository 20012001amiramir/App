package com.example.fu.ui.enter

import android.content.ActivityNotFoundException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fu.data.network.response.LoginResponse
import com.example.fu.interactor.SessionInteractors
import com.example.fu.util.ErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.fu.interactor.AccountInteractor
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class LoginViewModel @Inject constructor(
    private val sessionInteractors: SessionInteractors,
    private val errorHandler: ErrorHandler,
    private val accountInteractor: AccountInteractor
) : ViewModel() {

    private val _loginViewState: MutableLiveData<LoginViewState> = MutableLiveData(LoginViewState.BlankViewState)
    val loginViewState: LiveData<LoginViewState> get() = _loginViewState

    fun saveToken(accessToken: String, refreshToken: String){
        accountInteractor.changeAccount(accessToken,refreshToken)
    }

    fun authorize(email: String, password: String) {
        _loginViewState.value = LoginViewState.LoadingViewState
        viewModelScope.launch {
            try {
                _loginViewState.postValue(
                    LoginViewState.Data(sessionInteractors.authorize(email, password))
                )
            } catch (e: ActivityNotFoundException) {
                errorHandler.proceed(e)
            }
        }
    }

    sealed class LoginViewState {
        data class Data(val data: LoginResponse) : LoginViewState()
        object LoadingViewState : LoginViewState()
        object BlankViewState : LoginViewState()
    }
}