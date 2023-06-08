package com.example.fu.data.network

import com.example.fu.data.network.request.AddGarbageRequest
import com.example.fu.data.network.request.GetGarbageRequest
import com.example.fu.data.network.response.AddGarbageResponse
import com.example.fu.data.network.response.GarbagesResponse
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("Garbage/AddGarbageFromApiToUserHistory")
    suspend fun AddGarbageInfo(@Field("name") name: String?, @Field("garbageTypes") garbageTypes: List<Int>?,@Field("barcode") barcode: String?,@Field("image") image: String?,): AddGarbageResponse


    @GET("Garbage")
    suspend fun GetGarbageInfo(@Query("barcode") barcode: String?): AddGarbageResponse

    @GET("Garbage/GetGarbageTypes")
    suspend fun GetGarbageTypes(): AddGarbageResponse

    @GET("Garbage/GetGarbageInfoByAuthorizedUser")
    suspend fun GetGarbageInfoByAuthorizedUser( @Query("barcode") barcode: String?): AddGarbageResponse

    @GET("Garbage/GetGarbagesScanedByAuthorizedUser")
    suspend fun GetGarbagesScanedByAuthorizedUser(): GarbagesResponse

    @POST("Garbage/AddGarbageFromApiToUserHistory")
    suspend fun AddGarbageFromApiToUserHistory(@Body request: GetGarbageRequest): AddGarbageResponse

    @GET("Garbage/GetGarbageFromApiUserHistory")
    suspend fun GetGarbageFromApiUserHistory(): AddGarbageResponse

}