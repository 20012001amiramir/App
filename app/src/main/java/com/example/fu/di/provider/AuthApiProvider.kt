package com.example.fu.di.provider

import com.example.fu.data.network.AuthApi
import com.example.fu.di.qualifier.AuthApiUrl
import com.example.fu.di.qualifier.AuthOkHttpQualifier
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class AuthApiProvider @Inject constructor(
    @AuthOkHttpQualifier private val okHttpClient: OkHttpClient,
    private val moshi: Moshi,
    @AuthApiUrl private val authUrl: String
) : Provider<AuthApi> {

    override fun get(): AuthApi =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(authUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AuthApi::class.java)

}