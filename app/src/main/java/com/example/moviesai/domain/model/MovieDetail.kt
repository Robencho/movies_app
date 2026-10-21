package com.example.moviesai.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val rating: Double,
    val runtime: Int,
    val genres: List<String>,
    val cast: List<CastMember> = emptyList(),
    val similarMovies: List<Movie> = emptyList()
)
