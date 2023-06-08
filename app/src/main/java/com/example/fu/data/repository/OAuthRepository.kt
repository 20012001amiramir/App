package com.example.fu.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import com.example.fu.data.network.AuthApi
import com.example.fu.data.network.model.TechnicalError
import com.example.fu.data.network.response.LoginResponse
import com.example.fu.data.network.response.RegisterResponse
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.util.ifNotNull
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
class OAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val localStorage: LocalStorage
) {
    var accessToken: String? = null
        private set

    private val refreshTokenContext = newSingleThreadContext("RefreshTokenContext")
    private var refreshTokensJob: Job? = null


    suspend fun login(email: String, password: String): LoginResponse {
        return authApi.login(email, password)
    }

    suspend fun register(email: String, password: String, returnPassword: String): RegisterResponse {
        return authApi.register(email, password, returnPassword)
    }

    suspend fun refreshTokens() {
        // We confine to single-threaded context to fix concurrent access to the refreshTokensJob.
        // Solution is from the docs: https://kotlinlang.org/docs/reference/coroutines/shared-mutable-state-and-concurrency.html#thread-confinement-coarse-grained
        withContext(refreshTokenContext) {
            if (refreshTokensJob?.isActive == true) {
                // Just wait already running refresh
                refreshTokensJob?.join()
            } else {
                refreshTokensJob = launch {
                    refreshTokensInternal()
                }
            }
        }
    }

    private suspend fun refreshTokensInternal() {
        ifNotNull(
            localStorage.refreshToken,
            localStorage.dataAccounts
        ) { refreshToken, _ ->
            val tokenResponse = authApi.refreshToken(
                "Bearer $refreshToken"
            )
            localStorage.refreshToken = tokenResponse.data?.refreshToken
            accessToken = tokenResponse.data?.accessToken
            localStorage.possibleRefreshToken = null
        } ?: run {
            throw TechnicalError(401, "Unauthorized")
        }
    }
    fun isAuthorized() = localStorage.refreshToken != null

    fun changeToken(access: String?, refresh: String?) {
        accessToken = access
        localStorage.refreshToken = refresh
    }
}