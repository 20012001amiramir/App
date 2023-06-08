package com.example.fu.data.network.model

class TechnicalError(
    val code: Int,
    message: String
) : Throwable(message)