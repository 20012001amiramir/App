package com.example.fu.di.provider

import android.util.Log
import com.example.fu.BuildConfig
import com.example.fu.di.PrimitiveWrapper
import com.example.fu.di.qualifier.Hostname
import com.example.fu.di.qualifier.TrustAllCertificatesQualifier
import com.example.fu.util.getSSLSocketFactory
import com.example.fu.util.getTrustManagers
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class AuthOkHttpProvider @Inject constructor(
    @Hostname private val hostname: String,
    @TrustAllCertificatesQualifier private val trustAll: PrimitiveWrapper<Boolean>
): Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        return  OkHttpClient
            .Builder()
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor(
                    logger = object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            Log.d("OkHttp", message)
                        }
                    }
                ).setLevel(HttpLoggingInterceptor.Level.BODY))
            .apply {
                if (BuildConfig.DEBUG) {
                    val trustManagers = getTrustManagers(trustAll.value)
                    val sslSocketFactory = getSSLSocketFactory(trustManagers)

                    if (trustManagers != null && trustManagers.isNotEmpty() && sslSocketFactory != null) {
                        sslSocketFactory(sslSocketFactory, trustManagers[0])
                    }
                }
            }
            .build()
    }
    companion object {

        private const val READ_TIMEOUT_SECONDS = 120L
        private const val CONNECT_TIMEOUT_SECONDS = 120L
    }
}