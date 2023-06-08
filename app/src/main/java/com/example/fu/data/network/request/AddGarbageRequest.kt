package com.example.fu.data.network.request

import com.example.fu.data.network.response.DataToken
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddGarbageRequest(
    @Json(name = "name")
    @SerializedName("name")
    val name: String?,
    @Json(name = "garbageTypes")
    @SerializedName("garbageTypes")
    val garbageTypes: List<Int>?,
    @Json(name = "barcode")
    @SerializedName("barcode")
    val barcode: String?,
    @Json(name = "image")
    @SerializedName("image")
    val image: String?
)