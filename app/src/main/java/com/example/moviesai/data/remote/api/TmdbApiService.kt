package com.example.moviesai.data.remote.api

import com.example.moviesai.data.remote.dto.CreditsResponseDto
import com.example.moviesai.data.remote.dto.MovieDetailDto
import com.example.moviesai.data.remote.dto.PopularMoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): PopularMoviesResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): PopularMoviesResponseDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): PopularMoviesResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): PopularMoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "es-ES"
    ): MovieDetailDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "es-ES"
    ): CreditsResponseDto

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): PopularMoviesResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): PopularMoviesResponseDto
}
