package com.example.fu.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClassifyResponse(
    @Json(name = "predicted_class")
    val predictedClass: String?
)

