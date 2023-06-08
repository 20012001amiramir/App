package com.example.fu.di.provider

import com.example.fu.data.network.ModelApi
import com.example.fu.di.qualifier.AuthOkHttpQualifier
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ModelApiProvider @Inject constructor(
    @AuthOkHttpQualifier private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
): Provider<ModelApi> {

    override fun get(): ModelApi =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("http://77.91.77.242:69")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ModelApi::class.java)

}