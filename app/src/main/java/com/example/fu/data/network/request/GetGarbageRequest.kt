package com.example.fu.data.network.request

import com.example.fu.data.network.response.DataToken
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetGarbageRequest(
    @Json(name = "barcode")
    val barcode: String?
)