package com.example.moviesai.presentation.popular

import com.example.moviesai.domain.model.Movie

sealed interface PopularMoviesUiState {
    data object Loading : PopularMoviesUiState
    data class Success(val movies: List<Movie>) : PopularMoviesUiState
    data class Error(val message: String) : PopularMoviesUiState
}
