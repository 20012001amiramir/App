package com.example.fu.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "data")
    val data: DataToken?,
    @Json(name = "messages")
    val messages: List<String>?,
    @Json(name = "success")
    val success: Boolean
)

@JsonClass(generateAdapter = true)
data class DataToken(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "refreshToken")
    val refreshToken: String,
    @Json(name = "userId")
    val userId: Int
)