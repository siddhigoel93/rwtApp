package com.example.rwtapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rwtapp.data.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _newsState = MutableLiveData<UiState>()
    val newsState: LiveData<UiState> = _newsState

    fun fetchNews() {
        _newsState.value = UiState.Loading

        viewModelScope.launch {
            val result = repository.getTopHeadlines()

            result
                .onSuccess { articles ->
                    _newsState.value = UiState.Success(articles)
                }
                .onFailure { error ->
                    _newsState.value = UiState.Error(error.message ?: "Something went wrong")
                }
        }
    }
}