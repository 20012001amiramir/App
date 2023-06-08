package com.example.fu.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GarbagesResponse(
    @Json(name = "data")
    val data: List<DataTokenForGarbage>?,
    @Json(name = "messages")
    val messages: List<String>?,
    @Json(name = "success")
    val success: Boolean
)
