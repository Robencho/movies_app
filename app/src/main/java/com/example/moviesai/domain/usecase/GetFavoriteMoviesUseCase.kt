package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getFavoriteMovies()
}
