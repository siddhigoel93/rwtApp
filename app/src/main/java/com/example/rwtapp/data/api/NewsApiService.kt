package com.example.rwtapp.data.api

import com.example.rwtapp.data.model.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String = "technology",
        @Query("apiKey") apiKey: String
    ): NewsApiResponse

}