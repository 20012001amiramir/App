package com.example.fu.data.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddGarbageResponse(
    @Json(name = "data")
    val data: DataTokenForGarbage?,
    @Json(name = "messages")
    val messages: List<String>?,
    @Json(name = "success")
    val success: Boolean
)

@JsonClass(generateAdapter = true)
data class DataTokenForGarbage(
    @Json(name = "barcode")
    val barcode: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "garbageTypes")
    val garbageTypes: List<GarbageType>?,
    @Json(name = "imagePath")
    val imagePath: String?,
    @Json(name = "name")
    val name: String?
)

@JsonClass(generateAdapter = true)
data class GarbageType(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)