package com.example.moviesai.presentation.favorites

import com.example.moviesai.domain.model.Movie

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(val movies: List<Movie>) : FavoritesUiState
}
