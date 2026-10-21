package com.example.moviesai.data.mapper

import com.example.moviesai.data.remote.dto.GenreDto
import com.example.moviesai.data.remote.dto.MovieDetailDto
import com.example.moviesai.data.remote.dto.MovieDto
import com.example.moviesai.data.remote.dto.PopularMoviesResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieMapperTest {

    @Test
    fun `PopularMoviesResponseDto toDomain correctly maps all fields and formats image url`() {
        val dto = PopularMoviesResponseDto(
            page = 1,
            results = listOf(
                MovieDto(
                    id = 101,
                    title = "Test Movie",
                    overview = "Test overview",
                    posterPath = "/test_poster.jpg",
                    releaseDate = "2024-05-10",
                    voteAverage = 8.5
                )
            ),
            totalPages = 10,
            totalResults = 200
        )

        val domainMovies = dto.toDomain()

        assertEquals(1, domainMovies.size)
        val movie = domainMovies[0]
        assertEquals(101, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals("Test overview", movie.overview)
        assertTrue(movie.posterUrl.endsWith("/test_poster.jpg"))
        assertTrue(movie.posterUrl.startsWith("https://image.tmdb.org/t/p/w500"))
        assertEquals("2024-05-10", movie.releaseDate)
        assertEquals(8.5, movie.rating, 0.001)
    }

    @Test
    fun `MovieDto toDomain handles null fields gracefully`() {
        val dto = MovieDto(
            id = 202,
            title = null,
            overview = null,
            posterPath = null,
            releaseDate = null,
            voteAverage = null
        )

        val movie = dto.toDomain()

        assertEquals(202, movie.id)
        assertEquals("", movie.title)
        assertEquals("", movie.overview)
        assertEquals("", movie.posterUrl)
        assertEquals("", movie.releaseDate)
        assertEquals(0.0, movie.rating, 0.001)
    }

    @Test
    fun `MovieDetailDto toDomain correctly maps detail fields, urls and genres`() {
        val detailDto = MovieDetailDto(
            id = 500,
            title = "Dune: Part Two",
            overview = "Detailed overview...",
            posterPath = "/dune_poster.jpg",
            backdropPath = "/dune_backdrop.jpg",
            releaseDate = "2024-03-01",
            voteAverage = 8.3,
            runtime = 166,
            genres = listOf(
                GenreDto(id = 1, name = "Sci-Fi"),
                GenreDto(id = 2, name = "Adventure")
            )
        )

        val movieDetail = detailDto.toDomain()

        assertEquals(500, movieDetail.id)
        assertEquals("Dune: Part Two", movieDetail.title)
        assertEquals("Detailed overview...", movieDetail.overview)
        assertTrue(movieDetail.posterUrl.endsWith("/dune_poster.jpg"))
        assertTrue(movieDetail.backdropUrl.endsWith("/dune_backdrop.jpg"))
        assertEquals("2024-03-01", movieDetail.releaseDate)
        assertEquals(8.3, movieDetail.rating, 0.001)
        assertEquals(166, movieDetail.runtime)
        assertEquals(listOf("Sci-Fi", "Adventure"), movieDetail.genres)
    }

    @Test
    fun `MovieDetailDto toDomain handles null fields gracefully`() {
        val detailDto = MovieDetailDto(
            id = 600,
            title = null,
            overview = null,
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = null,
            runtime = null,
            genres = null
        )

        val movieDetail = detailDto.toDomain()

        assertEquals(600, movieDetail.id)
        assertEquals("", movieDetail.title)
        assertEquals("", movieDetail.overview)
        assertEquals("", movieDetail.posterUrl)
        assertEquals("", movieDetail.backdropUrl)
        assertEquals("", movieDetail.releaseDate)
        assertEquals(0.0, movieDetail.rating, 0.001)
        assertEquals(0, movieDetail.runtime)
        assertTrue(movieDetail.genres.isEmpty())
    }

    @Test
    fun `CreditsResponseDto toDomain correctly maps cast list and profile url`() {
        val creditsDto = com.example.moviesai.data.remote.dto.CreditsResponseDto(
            id = 500,
            cast = listOf(
                com.example.moviesai.data.remote.dto.CastDto(
                    id = 10,
                    name = "Timothée Chalamet",
                    character = "Paul Atreides",
                    profilePath = "/timothee.jpg"
                )
            )
        )

        val cast = creditsDto.toDomain()

        assertEquals(1, cast.size)
        val member = cast[0]
        assertEquals(10, member.id)
        assertEquals("Timothée Chalamet", member.name)
        assertEquals("Paul Atreides", member.character)
        assertTrue(member.profileUrl.endsWith("/timothee.jpg"))
        assertTrue(member.profileUrl.startsWith("https://image.tmdb.org/t/p/w185"))
    }

    @Test
    fun `MovieEntity and Movie mapping works bidirectionally`() {
        val movie = com.example.moviesai.domain.model.Movie(
            id = 42,
            title = "Test Favorite",
            overview = "Overview",
            posterUrl = "https://image.tmdb.org/t/p/w500/test.jpg",
            releaseDate = "2024-01-01",
            rating = 8.5
        )

        val entity = movie.toEntity(addedAt = 123456L)
        assertEquals(42, entity.id)
        assertEquals("Test Favorite", entity.title)
        assertEquals(123456L, entity.addedAt)

        val mappedMovie = entity.toDomain()
        assertEquals(movie.id, mappedMovie.id)
        assertEquals(movie.title, mappedMovie.title)
        assertEquals(movie.posterUrl, mappedMovie.posterUrl)
        assertEquals(movie.rating, mappedMovie.rating, 0.001)
    }

    @Test
    fun `MovieDetail toMovie maps basic movie fields`() {
        val detail = com.example.moviesai.domain.model.MovieDetail(
            id = 1,
            title = "Detail Title",
            overview = "Detail Overview",
            posterUrl = "poster.jpg",
            backdropUrl = "backdrop.jpg",
            releaseDate = "2024-01-01",
            rating = 9.0,
            runtime = 120,
            genres = listOf("Action")
        )

        val movie = detail.toMovie()
        assertEquals(1, movie.id)
        assertEquals("Detail Title", movie.title)
        assertEquals("poster.jpg", movie.posterUrl)
        assertEquals("backdrop.jpg", movie.backdropUrl)
        assertEquals(9.0, movie.rating, 0.001)
    }
}
