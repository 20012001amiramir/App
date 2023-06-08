package com.example.fu.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import com.example.fu.data.network.Api
import com.example.fu.util.valueAsUnique
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val api: Api
) {
    private val authErrorFlow = MutableStateFlow<Throwable?>(null)

    var minSupportedVersionCode: Int? = null

    fun authErrorFlow(): Flow<Throwable> = authErrorFlow.filterNotNull()

    fun proceedAuthError(authError: Throwable) {
        authErrorFlow.valueAsUnique = authError
    }

}