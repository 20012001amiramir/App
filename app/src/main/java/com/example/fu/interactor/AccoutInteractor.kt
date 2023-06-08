package com.example.fu.interactor

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fu.data.repository.AccountRepository
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class AccountInteractor @Inject constructor(
    private val repository: AccountRepository
) {

    fun changeAccount(access: String?, refreshToken: String?) {
        return repository.changeToken(access, refreshToken)
    }

    suspend fun deleteAccount(id: String) {
        repository.deleteToken(id)
    }

    suspend fun deleteCurrentAccount() {
        repository.deleteCurrentAccount()
    }

}