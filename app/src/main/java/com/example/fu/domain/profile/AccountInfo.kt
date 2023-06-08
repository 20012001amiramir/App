package com.example.fu.domain.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountInfo(
    @Json(name = "id") val id: String,
    @Json(name = "refreshToken") var refreshToken: String?,
    @Json(name = "accessToken") var accessToken: String?,
    @Json(name = "fullName") var fullName: String,
    @Json(name = "photoProfile") var photoProfile: String?,
    @Json(name = "titleClass") var titleClass: String?,
    @Json(name = "currentProfile") var currentProfile: Boolean = false
)


