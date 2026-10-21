package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(query: String, page: Int = 1): Flow<Result<List<Movie>>> = flow {
        emit(repository.searchMovies(query, page))
    }
}
