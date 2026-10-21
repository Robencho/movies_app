package com.example.moviesai.data.repository

import com.example.moviesai.data.local.dao.MovieDao
import com.example.moviesai.data.local.entity.MovieEntity
import com.example.moviesai.data.remote.api.TmdbApiService
import com.example.moviesai.data.remote.dto.GenreDto
import com.example.moviesai.data.remote.dto.MovieDetailDto
import com.example.moviesai.data.remote.dto.MovieDto
import com.example.moviesai.data.remote.dto.PopularMoviesResponseDto
import com.example.moviesai.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class MovieRepositoryImplTest {

    private class FakeMovieDao : MovieDao {
        private val list = mutableListOf<MovieEntity>()
        private val flow = MutableStateFlow<List<MovieEntity>>(emptyList())

        override fun getFavoriteMovies(): Flow<List<MovieEntity>> = flow

        override suspend fun isFavorite(movieId: Int): Boolean = list.any { it.id == movieId }

        override suspend fun insertFavorite(movie: MovieEntity) {
            list.removeAll { it.id == movie.id }
            list.add(movie)
            flow.value = list.toList()
        }

        override suspend fun deleteFavorite(movieId: Int) {
            list.removeAll { it.id == movieId }
            flow.value = list.toList()
        }
    }

    private class FakeTmdbApiService(
        private val shouldThrow: Boolean = false
    ) : TmdbApiService {
        private fun dummyResponse(title: String) = PopularMoviesResponseDto(
            page = 1,
            results = listOf(
                MovieDto(
                    id = 1,
                    title = title,
                    overview = "Description",
                    posterPath = "/sample.jpg",
                    backdropPath = "/backdrop.jpg",
                    releaseDate = "2024-01-01",
                    voteAverage = 7.5
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        override suspend fun getPopularMovies(page: Int, language: String): PopularMoviesResponseDto {
            if (shouldThrow) throw IOException("Network error")
            return dummyResponse("Popular Movie")
        }

        override suspend fun getNowPlayingMovies(page: Int, language: String): PopularMoviesResponseDto {
            if (shouldThrow) throw IOException("Network error now playing")
            return dummyResponse("Now Playing Movie")
        }

        override suspend fun getUpcomingMovies(page: Int, language: String): PopularMoviesResponseDto {
            if (shouldThrow) throw IOException("Network error upcoming")
            return dummyResponse("Upcoming Movie")
        }

        override suspend fun getTopRatedMovies(page: Int, language: String): PopularMoviesResponseDto {
            if (shouldThrow) throw IOException("Network error top rated")
            return dummyResponse("Top Rated Movie")
        }

        override suspend fun getMovieDetail(movieId: Int, language: String): MovieDetailDto {
            if (shouldThrow) {
                throw IOException("Network error detail")
            }
            return MovieDetailDto(
                id = movieId,
                title = "Detailed Movie",
                overview = "Detailed Description",
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                releaseDate = "2024-01-01",
                voteAverage = 8.0,
                runtime = 120,
                genres = listOf(GenreDto(id = 1, name = "Action"))
            )
        }

        override suspend fun getMovieCredits(movieId: Int, language: String): com.example.moviesai.data.remote.dto.CreditsResponseDto {
            if (shouldThrow) throw IOException("Network error credits")
            return com.example.moviesai.data.remote.dto.CreditsResponseDto(
                id = movieId,
                cast = listOf(
                    com.example.moviesai.data.remote.dto.CastDto(
                        id = 1,
                        name = "Actor 1",
                        character = "Role 1",
                        profilePath = "/actor.jpg"
                    )
                )
            )
        }

        override suspend fun getSimilarMovies(movieId: Int, page: Int, language: String): PopularMoviesResponseDto {
            if (shouldThrow) throw IOException("Network error similar")
            return dummyResponse("Similar Movie")
        }

        override suspend fun searchMovies(query: String, page: Int, language: String): PopularMoviesResponseDto {
            if (shouldThrow) throw IOException("Network error search")
            return dummyResponse("Searched Movie: $query")
        }
    }

    @Test
    fun `getPopularMovies returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getPopularMovies(page = 1)

        assertTrue(result.isSuccess)
        val movies = result.getOrThrow()
        assertEquals(1, movies.size)
        assertEquals("Popular Movie", movies[0].title)
    }

    @Test
    fun `getNowPlayingMovies returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getNowPlayingMovies(page = 1)

        assertTrue(result.isSuccess)
        val movies = result.getOrThrow()
        assertEquals(1, movies.size)
        assertEquals("Now Playing Movie", movies[0].title)
    }

    @Test
    fun `getUpcomingMovies returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getUpcomingMovies(page = 1)

        assertTrue(result.isSuccess)
        val movies = result.getOrThrow()
        assertEquals(1, movies.size)
        assertEquals("Upcoming Movie", movies[0].title)
    }

    @Test
    fun `getTopRatedMovies returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getTopRatedMovies(page = 1)

        assertTrue(result.isSuccess)
        val movies = result.getOrThrow()
        assertEquals(1, movies.size)
        assertEquals("Top Rated Movie", movies[0].title)
    }

    @Test
    fun `getMovieDetail returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getMovieDetail(movieId = 42)

        assertTrue(result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(42, detail.id)
        assertEquals("Detailed Movie", detail.title)
    }

    @Test
    fun `getMovieCredits returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getMovieCredits(movieId = 42)

        assertTrue(result.isSuccess)
        val cast = result.getOrThrow()
        assertEquals(1, cast.size)
        assertEquals("Actor 1", cast[0].name)
    }

    @Test
    fun `getSimilarMovies returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.getSimilarMovies(movieId = 42)

        assertTrue(result.isSuccess)
        val movies = result.getOrThrow()
        assertEquals(1, movies.size)
        assertEquals("Similar Movie", movies[0].title)
    }

    @Test
    fun `searchMovies returns success result when api call succeeds`() = runTest {
        val fakeApi = FakeTmdbApiService(shouldThrow = false)
        val repository = MovieRepositoryImpl(fakeApi, FakeMovieDao())

        val result = repository.searchMovies(query = "Avatar")

        assertTrue(result.isSuccess)
        val movies = result.getOrThrow()
        assertEquals("Searched Movie: Avatar", movies[0].title)
    }

    @Test
    fun `toggleFavorite and isFavorite work correctly with Room dao`() = runTest {
        val fakeDao = FakeMovieDao()
        val repository = MovieRepositoryImpl(FakeTmdbApiService(), fakeDao)
        val movie = Movie(100, "Favorited Movie", "Overview", "", "2024", 8.0)

        assertFalse(repository.isFavorite(100))

        repository.toggleFavorite(movie)
        assertTrue(repository.isFavorite(100))
        val favorites = repository.getFavoriteMovies().first()
        assertEquals(1, favorites.size)
        assertEquals("Favorited Movie", favorites[0].title)

        repository.toggleFavorite(movie)
        assertFalse(repository.isFavorite(100))
    }
}
