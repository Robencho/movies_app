package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Result<MovieDetail>> = flow {
        coroutineScope {
            val detailDeferred = async { repository.getMovieDetail(movieId) }
            val creditsDeferred = async { repository.getMovieCredits(movieId) }
            val similarDeferred = async { repository.getSimilarMovies(movieId) }

            val detailResult = detailDeferred.await()
            val creditsResult = creditsDeferred.await()
            val similarResult = similarDeferred.await()

            val combinedResult = detailResult.map { detail ->
                detail.copy(
                    cast = creditsResult.getOrDefault(emptyList()),
                    similarMovies = similarResult.getOrDefault(emptyList())
                )
            }

            emit(combinedResult)
        }
    }
}
