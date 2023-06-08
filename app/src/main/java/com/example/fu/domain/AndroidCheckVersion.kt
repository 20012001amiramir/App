package com.example.fu.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AndroidCheckVersion(
    @Json(name = "applicationCode") val applicationCode: Int
)