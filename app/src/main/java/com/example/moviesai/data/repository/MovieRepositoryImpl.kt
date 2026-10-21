package com.example.moviesai.data.repository

import com.example.moviesai.data.local.dao.MovieDao
import com.example.moviesai.data.mapper.toDomain
import com.example.moviesai.data.mapper.toEntity
import com.example.moviesai.data.remote.api.TmdbApiService
import com.example.moviesai.domain.model.CastMember
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return runCatching {
            val response = apiService.getPopularMovies(page = page)
            response.toDomain()
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): Result<List<Movie>> {
        return runCatching {
            val response = apiService.getNowPlayingMovies(page = page)
            response.toDomain()
        }
    }

    override suspend fun getUpcomingMovies(page: Int): Result<List<Movie>> {
        return runCatching {
            val response = apiService.getUpcomingMovies(page = page)
            response.toDomain()
        }
    }

    override suspend fun getTopRatedMovies(page: Int): Result<List<Movie>> {
        return runCatching {
            val response = apiService.getTopRatedMovies(page = page)
            response.toDomain()
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> {
        return runCatching {
            val response = apiService.getMovieDetail(movieId = movieId)
            response.toDomain()
        }
    }

    override suspend fun getMovieCredits(movieId: Int): Result<List<CastMember>> {
        return runCatching {
            val response = apiService.getMovieCredits(movieId = movieId)
            response.toDomain()
        }
    }

    override suspend fun getSimilarMovies(movieId: Int, page: Int): Result<List<Movie>> {
        return runCatching {
            val response = apiService.getSimilarMovies(movieId = movieId, page = page)
            response.toDomain()
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return movieDao.isFavorite(movieId)
    }

    override suspend fun toggleFavorite(movie: Movie) {
        if (movieDao.isFavorite(movie.id)) {
            movieDao.deleteFavorite(movie.id)
        } else {
            movieDao.insertFavorite(movie.toEntity())
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<List<Movie>> {
        return runCatching {
            val response = apiService.searchMovies(query = query, page = page)
            response.toDomain()
        }
    }
}
