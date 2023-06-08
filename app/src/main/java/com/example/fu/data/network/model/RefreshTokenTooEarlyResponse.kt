package com.example.fu.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenTooEarlyResponse(
    @Json(name = "access_token") val access_token: String,
    @Json(name = "refresh_token") val refresh_token: String
)
