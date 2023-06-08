package com.example.fu.data.network

import com.example.fu.data.network.response.LoginResponse
import com.example.fu.data.network.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.fu.data.network.response.TokenResponse
import retrofit2.http.Header

interface AuthApi {

    @FormUrlEncoded
    @POST("/Account/Register")
    suspend fun register(
        @Field("Email") Email: String,
        @Field("Password") Password: String,
        @Field("PasswordRepeat") PasswordRepeat: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/Account/Login")
    suspend fun login(
        @Field("Email") Email: String,
        @Field("Password") Password: String,
    ): LoginResponse

    @POST("/Account/RefreshToken")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): LoginResponse

}