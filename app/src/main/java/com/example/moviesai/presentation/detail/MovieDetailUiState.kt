package com.example.moviesai.presentation.detail

import com.example.moviesai.domain.model.MovieDetail

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movie: MovieDetail) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}
