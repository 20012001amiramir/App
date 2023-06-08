package com.example.fu.util

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import com.example.fu.data.network.model.RefreshTokenError
import com.example.fu.data.network.model.RefreshTokenTooEarly
import com.example.fu.data.network.model.SilentError
import com.example.fu.data.network.model.TechnicalError
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.domain.profile.AccountInfo
import timber.log.Timber
import javax.inject.Inject

// TODO: Discuss removal of this class.
class ErrorHandler @Inject constructor(
    private val localStorage: LocalStorage
) {

    private val globalErrorFlow = MutableStateFlow<GlobalErrorState?>(null)

    fun getGlobalErrorFlow() = globalErrorFlow

    fun proceed(error: Throwable, humanReadableMessage: String? = null, showAlert: Boolean = true) {
        //Robolog.w(humanReadableMessage ?: "", Meta(error ?: Throwable("no throwable")))
        Timber.w(error)
        Log.i("handleIntent", "proceed ${error}")

        when {
            error is CancellationException -> return
            error is HttpException && (error.code() == 401 || error.code() == 403) -> {
                localStorage.clear()
                globalErrorFlow.valueAsUnique = GlobalErrorState.UnauthorizedErrorState(
                    TechnicalError(403, "Unauthorized")
                )
            }
            error is RefreshTokenError && (error.errorType == "invalid_grant") -> {
                deleteRefreshToken(localStorage.possibleRefreshToken)
                globalErrorFlow.valueAsUnique = GlobalErrorState.UnauthorizedErrorState(
                    TechnicalError(403, error.message ?: "")
                )
            }
            error is RefreshTokenTooEarly && (error.refresh_token != null) -> {
                globalErrorFlow.valueAsUnique = GlobalErrorState.TokenActiveErrorState(
                    error
                )
            }
            error is RefreshTokenError -> {
                localStorage.clear()
                globalErrorFlow.valueAsUnique = GlobalErrorState.UpdateRequiredErrorState(
                    TechnicalError(
                        400,
                        error.message ?: ""
                    )
                )
            }
            error is SilentError -> { // TODO: (Idea) Can be refactored to use preceed with showAlert = false and just pass raw exception here. Or check it before `when`
                //do nothing, but log this error to timber and robolog in 29,30
            }
            else -> {
                globalErrorFlow.value = GlobalErrorState.ReadableErrorState(error)
            }
        }
    }

    fun notifyErrorHandled() {
        globalErrorFlow.value = null
    }

    private fun deleteRefreshToken(refresh: String?) {
        val accounts = localStorage.dataAccounts
            ?.fromJson<MutableList<AccountInfo>>()
        val possibleDeleteAccount = accounts?.find { it.refreshToken == refresh }
        possibleDeleteAccount?.refreshToken = null
        localStorage.dataAccounts = accounts?.toJson()
    }

    sealed class GlobalErrorState {
        class UnauthorizedErrorState(val error: TechnicalError) : GlobalErrorState()
        class TokenActiveErrorState(val error: RefreshTokenTooEarly) : GlobalErrorState()
        class UpdateRequiredErrorState(val error: TechnicalError) : GlobalErrorState()
        class ReadableErrorState(val error: Throwable) : GlobalErrorState()
    }
}