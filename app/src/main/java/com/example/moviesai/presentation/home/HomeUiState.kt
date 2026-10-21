package com.example.moviesai.presentation.home

import com.example.moviesai.domain.model.Movie

data class HomeContent(
    val featuredMovie: Movie?,
    val nowPlaying: List<Movie>,
    val popular: List<Movie>,
    val topRated: List<Movie>,
    val upcoming: List<Movie>
)

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val content: HomeContent) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
