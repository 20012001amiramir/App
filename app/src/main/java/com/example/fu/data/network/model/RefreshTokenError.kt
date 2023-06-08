package com.example.fu.data.network.model

data class RefreshTokenError(
    val errorType: String,
    val errorDescription: String
) : Throwable("errorType: $errorType message: $errorDescription")