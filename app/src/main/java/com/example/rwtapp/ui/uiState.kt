package com.example.rwtapp.ui

import com.example.rwtapp.data.model.Article

sealed class UiState {
    object Loading : UiState()
    data class Success(val articles: List<Article>) : UiState()
    data class Error(val message: String) : UiState()
}