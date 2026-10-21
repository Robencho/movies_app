package com.example.moviesai.data.mapper

import com.example.moviesai.BuildConfig
import com.example.moviesai.data.remote.dto.CastDto
import com.example.moviesai.data.remote.dto.CreditsResponseDto
import com.example.moviesai.data.remote.dto.MovieDetailDto
import com.example.moviesai.data.remote.dto.MovieDto
import com.example.moviesai.data.remote.dto.PopularMoviesResponseDto
import com.example.moviesai.domain.model.CastMember
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail

private const val DEFAULT_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val DEFAULT_PROFILE_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185"

private fun buildImageUrl(path: String?, baseUrl: String): String {
    return if (!path.isNullOrBlank()) {
        if (path.startsWith("/")) {
            "$baseUrl$path"
        } else {
            "$baseUrl/$path"
        }
    } else {
        ""
    }
}

fun PopularMoviesResponseDto.toDomain(): List<Movie> {
    return results?.map { it.toDomain() }.orEmpty()
}

fun MovieDto.toDomain(): Movie {
    val imageBaseUrl = try {
        BuildConfig.TMDB_IMAGE_BASE_URL
    } catch (_: Throwable) {
        DEFAULT_IMAGE_BASE_URL
    }

    return Movie(
        id = id,
        title = title.orEmpty(),
        overview = overview.orEmpty(),
        posterUrl = buildImageUrl(posterPath, imageBaseUrl),
        backdropUrl = buildImageUrl(backdropPath, imageBaseUrl),
        releaseDate = releaseDate.orEmpty(),
        rating = voteAverage ?: 0.0
    )
}

fun MovieDetailDto.toDomain(): MovieDetail {
    val imageBaseUrl = try {
        BuildConfig.TMDB_IMAGE_BASE_URL
    } catch (_: Throwable) {
        DEFAULT_IMAGE_BASE_URL
    }

    return MovieDetail(
        id = id,
        title = title.orEmpty(),
        overview = overview.orEmpty(),
        posterUrl = buildImageUrl(posterPath, imageBaseUrl),
        backdropUrl = buildImageUrl(backdropPath, imageBaseUrl),
        releaseDate = releaseDate.orEmpty(),
        rating = voteAverage ?: 0.0,
        runtime = runtime ?: 0,
        genres = genres?.mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } }.orEmpty()
    )
}

fun CreditsResponseDto.toDomain(): List<CastMember> {
    return cast?.map { it.toDomain() }.orEmpty()
}

fun CastDto.toDomain(): CastMember {
    return CastMember(
        id = id,
        name = name.orEmpty(),
        character = character.orEmpty(),
        profileUrl = buildImageUrl(profilePath, DEFAULT_PROFILE_IMAGE_BASE_URL)
    )
}

fun com.example.moviesai.data.local.entity.MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        releaseDate = releaseDate,
        rating = rating
    )
}

fun Movie.toEntity(addedAt: Long = System.currentTimeMillis()): com.example.moviesai.data.local.entity.MovieEntity {
    return com.example.moviesai.data.local.entity.MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        releaseDate = releaseDate,
        rating = rating,
        addedAt = addedAt
    )
}

fun MovieDetail.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        rating = rating
    )
}
