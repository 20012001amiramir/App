package com.example.fu.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.domain.profile.AccountInfo
import com.example.fu.util.fromJson
import com.example.fu.util.toJson
import com.example.fu.util.valueAsUnique
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class AccountRepository @Inject constructor(
    private val oAuthRepository: OAuthRepository,
    private val localStorage: LocalStorage
) {

    fun changeToken(accessToken: String?, refreshToken: String?) {
            localStorage.possibleRefreshToken = refreshToken
            oAuthRepository.changeToken(accessToken, refreshToken)
    }


    suspend fun deleteToken(id: String) {
        return withContext(NonCancellable) {
            val accounts = localStorage.dataAccounts
                ?.fromJson<MutableList<AccountInfo>>()
            val deleteAccount = accounts?.find {
                it.id == id
            }
            accounts?.remove(deleteAccount)
            if (deleteAccount?.currentProfile == true) {
                accounts.first().refreshToken?.let { changeToken(accounts.first().accessToken, it) }
                accounts.first().currentProfile = true
            }
            localStorage.dataAccounts = accounts?.toJson()
        }
    }

    suspend fun deleteCurrentAccount() {
        return withContext(NonCancellable) {
            val accounts = localStorage.dataAccounts
                ?.fromJson<MutableList<AccountInfo>>()
            accounts?.let { accounts ->
                accounts.removeIf { it.currentProfile }
                if (accounts.isNotEmpty()) {
                        changeToken(
                            accounts.first().accessToken,
                            accounts.first().refreshToken
                        )
                    accounts.first().currentProfile = true
                } else {

                }
            }
            localStorage.dataAccounts = accounts?.toJson()
        }
    }


}