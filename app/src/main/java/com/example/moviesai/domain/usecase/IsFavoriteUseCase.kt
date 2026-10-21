package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.repository.MovieRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean = repository.isFavorite(movieId)
}
