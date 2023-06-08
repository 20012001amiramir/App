package com.example.fu.model

data class Disease(
    val id: Int,
    val plantName: String,
    val diseaseName: String,
    val description: String,
    val sourceName: String,
    val urlToImages: List<String>,
    val urlToSource: String,
    val danger: String,
    val type: String,
    val symptoms: List<String>,
    val treatment: String
)

