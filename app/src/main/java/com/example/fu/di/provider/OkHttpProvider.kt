package com.example.fu.di.provider

import android.util.Log
import com.example.fu.data.network.interceptor.RequestIdInterceptor
import com.example.fu.data.network.interceptor.TokenAuthenticator
import com.example.fu.data.network.interceptor.TokenInterceptor
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

class OkHttpProvider @Inject constructor(
    private val tokenInterceptor: TokenInterceptor,
    private val requestIdInterceptor: RequestIdInterceptor,
    private val tokenAuthenticator: TokenAuthenticator,
    @Hostname private val hostname: String,
    @TrustAllCertificatesQualifier private val trustAll: PrimitiveWrapper<Boolean>
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        val certificatePinner = CertificatePinner.Builder()
            .add(
                "*.tatar.ru",
                "sha256/0wY9JmIixdGKz7TA4EXgKPROTzV0x1nLcoDTt2QZmJ0=",
                "sha256/yZU/dsuGMDS0U2+JMsQ1PVuvek1Hj0TNgElqu8hv1rw="
            )
            .build()

        return OkHttpClient
            .Builder()
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(requestIdInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(
                HttpLoggingInterceptor(
                    logger = object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            //Robolog.d("OkHttp", message)
                            Log.d("OkHttp", message)
                        }
                    }
                ).setLevel(HttpLoggingInterceptor.Level.BODY))
            .apply {
                val trustManagers = getTrustManagers(trustAll.value)
                val sslSocketFactory = getSSLSocketFactory(trustManagers)

                if (trustManagers != null && trustManagers.isNotEmpty() && sslSocketFactory != null) {
                    sslSocketFactory(sslSocketFactory, trustManagers[0])
                }

            }
            .build()
    }


    companion object {

        private const val READ_TIMEOUT_SECONDS = 120L
        private const val CONNECT_TIMEOUT_SECONDS = 120L
    }
}