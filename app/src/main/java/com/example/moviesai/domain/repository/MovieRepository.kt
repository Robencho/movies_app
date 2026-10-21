package com.example.moviesai.domain.repository

import com.example.moviesai.domain.model.CastMember
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): Result<List<Movie>>
    suspend fun getNowPlayingMovies(page: Int = 1): Result<List<Movie>> = Result.success(emptyList())
    suspend fun getUpcomingMovies(page: Int = 1): Result<List<Movie>> = Result.success(emptyList())
    suspend fun getTopRatedMovies(page: Int = 1): Result<List<Movie>> = Result.success(emptyList())
    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>
    suspend fun getMovieCredits(movieId: Int): Result<List<CastMember>> = Result.success(emptyList())
    suspend fun getSimilarMovies(movieId: Int, page: Int = 1): Result<List<Movie>> = Result.success(emptyList())

    // Room Favorites
    fun getFavoriteMovies(): Flow<List<Movie>> = flowOf(emptyList())
    suspend fun isFavorite(movieId: Int): Boolean = false
    suspend fun toggleFavorite(movie: Movie) {}

    // Search
    suspend fun searchMovies(query: String, page: Int = 1): Result<List<Movie>> = Result.success(emptyList())
}
