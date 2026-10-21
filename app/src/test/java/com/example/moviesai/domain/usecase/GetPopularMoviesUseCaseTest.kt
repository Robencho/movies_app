package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private class FakeMovieRepository(
        private val result: Result<List<Movie>>
    ) : MovieRepository {
        override suspend fun getPopularMovies(page: Int): Result<List<Movie>> = result
        override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> {
            return Result.failure(NotImplementedError())
        }
    }

    @Test
    fun `use case emits repository result successfully`() = runTest {
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "Interstellar",
                overview = "Space exploration",
                posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
                releaseDate = "2014-11-07",
                rating = 8.7
            )
        )
        val fakeRepository = FakeMovieRepository(Result.success(expectedMovies))
        val useCase = GetPopularMoviesUseCase(fakeRepository)

        val emission = useCase(page = 1).first()

        assertTrue(emission.isSuccess)
        assertEquals(expectedMovies, emission.getOrNull())
    }

    @Test
    fun `use case emits error when repository fails`() = runTest {
        val fakeRepository = FakeMovieRepository(Result.failure(RuntimeException("Error loading")))
        val useCase = GetPopularMoviesUseCase(fakeRepository)

        val emission = useCase(page = 1).first()

        assertTrue(emission.isFailure)
        assertEquals("Error loading", emission.exceptionOrNull()?.message)
    }
}
