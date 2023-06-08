package com.example.fu.di

import android.content.Context
import com.example.fu.BuildConfig
import com.example.fu.data.network.Api
import com.example.fu.data.network.AuthApi
import com.example.fu.data.network.ModelApi
import com.example.fu.data.network.interceptor.RequestIdInterceptor
import com.example.fu.data.network.interceptor.TokenAuthenticator
import com.example.fu.data.network.interceptor.TokenInterceptor
import com.example.fu.data.persistent.LocalStorage
import com.example.fu.data.repository.CategoryRepository
import com.example.fu.data.repository.DiseaseRepository
import com.example.fu.data.repository.ModelRepository
import com.example.fu.data.repository.NewsRepository
import com.example.fu.data.repository.OAuthRepository
import com.example.fu.data.repository.SessionRepository
import com.example.fu.di.provider.ApiProvider
import com.example.fu.di.provider.AuthApiProvider
import com.example.fu.di.provider.AuthOkHttpProvider
import com.example.fu.di.provider.ModelApiProvider
import com.example.fu.di.provider.MoshiProvider
import com.example.fu.di.provider.OkHttpProvider
import com.example.fu.di.qualifier.AuthApiUrl
import com.example.fu.di.qualifier.AuthOkHttpQualifier
import com.example.fu.di.qualifier.Hostname
import com.example.fu.di.qualifier.OkHttpQualifier
import com.example.fu.di.qualifier.ServerApiUrl
import com.example.fu.di.qualifier.TrustAllCertificatesQualifier
import com.example.fu.interactor.SessionInteractors
import com.example.fu.util.ErrorHandler
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import toothpick.config.Module

class AppModule(context: Context) : Module() {

    init {
        bind(Context::class.java).toInstance(context)

        bind(String::class.java)
            .withName(Hostname::class.java)
            .toInstance(BuildConfig.HOSTNAME)

        bind(String::class.java)
            .withName(ServerApiUrl::class.java)
            .toInstance(BuildConfig.SERVER_API_URL)

        bind(Moshi::class.java)
            .toProvider(MoshiProvider::class.java)

        bind(String::class.java)
            .withName(AuthApiUrl::class.java)
            .toInstance(BuildConfig.AUTH_API_URL)

        bind(PrimitiveWrapper::class.java)
            .withName(TrustAllCertificatesQualifier::class.java)
            .toInstance(PrimitiveWrapper(BuildConfig.TRUST_ALL_CERTIFICATES))

        bind(OkHttpClient::class.java)
            .withName(OkHttpQualifier::class.java)
            .toProvider(OkHttpProvider::class.java)
            .providesSingleton()

        bind(OkHttpClient::class.java)
            .withName(AuthOkHttpQualifier::class.java)
            .toProvider(AuthOkHttpProvider::class.java)
            .providesSingleton()

        bind(Api::class.java)
            .toProvider(ApiProvider::class.java)
            .providesSingleton()

        bind(AuthApi::class.java)
            .toProvider(AuthApiProvider::class.java)
            .providesSingleton()

        bind(ModelApi::class.java)
            .toProvider(ModelApiProvider::class.java)
            .providesSingleton()

        bind(SessionInteractors::class.java)

        bind(OAuthRepository::class.java).singleton()
        bind(SessionRepository::class.java)

        bind(LocalStorage::class.java).singleton()

        bind(TokenInterceptor::class.java).singleton()
        bind(TokenAuthenticator::class.java).singleton()
        bind(RequestIdInterceptor::class.java).singleton()

        bind(ErrorHandler::class.java).singleton()

        bind(CategoryRepository::class.java).singleton()
        bind(DiseaseRepository::class.java).singleton()
        bind(NewsRepository::class.java).singleton()
        bind(ModelRepository::class.java).singleton()
    }
}