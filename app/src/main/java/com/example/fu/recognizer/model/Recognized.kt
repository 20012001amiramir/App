package com.example.fu.recognizer.model

import java.util.*

data class Recognized(
    val id: String,
    val image: String,
    val date: Date,
    val recognizer: RecognizerName,
    val emotions: Map<String, Int>
) : java.io.Serializable