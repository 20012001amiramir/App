package com.example.fu.data.network

import com.example.fu.data.network.response.ClassifyResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ModelApi {

    @Multipart
    @POST("predict?image")
    suspend fun classifyDisease(
        @Part image: MultipartBody.Part
    ): ClassifyResponse

}