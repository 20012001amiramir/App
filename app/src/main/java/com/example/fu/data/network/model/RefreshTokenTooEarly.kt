package com.example.fu.data.network.model

data class RefreshTokenTooEarly(
    val access_token: String?,
    val refresh_token: String?
) : Throwable(
    "access_token: $access_token refresh_token: $refresh_token"
)