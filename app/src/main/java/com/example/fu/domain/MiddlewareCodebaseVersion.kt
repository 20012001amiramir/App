package com.example.fu.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MiddlewareCodebaseVersion(
    @Json(name = "apiVersion") val mwVersion: String,
)