package com.example.fu.model

data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val urlToImages: List<String>
)
