package com.example.fu.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenErrorResponse(
    @Json(name = "error") val errorType: String,
    @Json(name = "error_description") val errorDescription: String
)