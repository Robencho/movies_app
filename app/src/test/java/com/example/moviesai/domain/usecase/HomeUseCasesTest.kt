package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUseCasesTest {

    private class FakeCategoryRepository : MovieRepository {
        val sampleMovie = Movie(
            id = 1,
            title = "Sample Movie",
            overview = "Overview",
            posterUrl = "",
            backdropUrl = "",
            releaseDate = "2024-01-01",
            rating = 8.0
        )

        override suspend fun getPopularMovies(page: Int): Result<List<Movie>> =
            Result.success(listOf(sampleMovie.copy(title = "Popular")))

        override suspend fun getNowPlayingMovies(page: Int): Result<List<Movie>> =
            Result.success(listOf(sampleMovie.copy(title = "Now Playing")))

        override suspend fun getUpcomingMovies(page: Int): Result<List<Movie>> =
            Result.success(listOf(sampleMovie.copy(title = "Upcoming")))

        override suspend fun getTopRatedMovies(page: Int): Result<List<Movie>> =
            Result.success(listOf(sampleMovie.copy(title = "Top Rated")))

        override suspend fun getMovieDetail(movieId: Int) =
            Result.failure<com.example.moviesai.domain.model.MovieDetail>(NotImplementedError())
    }

    @Test
    fun `GetNowPlayingMoviesUseCase emits repository now playing movies`() = runTest {
        val useCase = GetNowPlayingMoviesUseCase(FakeCategoryRepository())
        val result = useCase().first()
        assertTrue(result.isSuccess)
        assertEquals("Now Playing", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `GetUpcomingMoviesUseCase emits repository upcoming movies`() = runTest {
        val useCase = GetUpcomingMoviesUseCase(FakeCategoryRepository())
        val result = useCase().first()
        assertTrue(result.isSuccess)
        assertEquals("Upcoming", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `GetTopRatedMoviesUseCase emits repository top rated movies`() = runTest {
        val useCase = GetTopRatedMoviesUseCase(FakeCategoryRepository())
        val result = useCase().first()
        assertTrue(result.isSuccess)
        assertEquals("Top Rated", result.getOrNull()?.first()?.title)
    }
}
