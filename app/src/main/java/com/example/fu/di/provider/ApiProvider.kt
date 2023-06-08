package com.example.fu.di.provider

import com.example.fu.data.network.Api
import com.example.fu.di.PrimitiveWrapper
import com.example.fu.di.qualifier.OkHttpQualifier
import com.example.fu.di.qualifier.ServerApiUrl
import com.example.fu.di.qualifier.UseMockApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    @OkHttpQualifier private val okHttpClient: OkHttpClient,
    private val moshi: Moshi,
    @ServerApiUrl private val baseUrl: String
) : Provider<Api> {

    override fun get(): Api =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

}