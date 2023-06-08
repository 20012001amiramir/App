package com.example.fu.data.network.interceptor

import com.example.fu.data.repository.OAuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class RequestIdInterceptor @Inject constructor(
    private val oAuthRepository: OAuthRepository,
    ) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request().let { request ->
            chain.proceed(
                oAuthRepository.accessToken
                    ?.let { token ->
                        request
                            .newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build()
                    }
                    ?: request
            )
        }
}