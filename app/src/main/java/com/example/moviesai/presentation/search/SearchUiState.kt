package com.example.moviesai.presentation.search

import com.example.moviesai.domain.model.Movie

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Empty : SearchUiState
    data class Success(val movies: List<Movie>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}
