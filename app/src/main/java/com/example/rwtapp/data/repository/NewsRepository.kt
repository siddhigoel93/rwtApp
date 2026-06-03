package com.example.rwtapp.data.repository

import com.example.rwtapp.data.model.Article
import com.example.rwtapp.data.api.ApiClient
import com.example.rwtapp.BuildConfig

class NewsRepository {

    suspend fun getTopHeadlines(country: String?, query: String?): Result<List<Article>> {
        return try {
            val response = ApiClient.newsApiService.getTopHeadlines(
                country = country,
                category = "technology",
                query = query,
                apiKey = BuildConfig.NEWS_API_KEY
            )

            if (response.isSuccessful) {
                val articles = response.body()?.articles.orEmpty()
                Result.success(articles)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}