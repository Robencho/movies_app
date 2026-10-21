package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) = repository.toggleFavorite(movie)
}
