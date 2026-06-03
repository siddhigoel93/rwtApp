package com.example.rwtapp.data.model

data class NewsApiResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)