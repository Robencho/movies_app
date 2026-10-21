package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.CastMember
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetMovieDetailUseCaseTest {

    private class FakeMovieRepository(
        private val detailResult: Result<MovieDetail>,
        private val creditsResult: Result<List<CastMember>> = Result.success(emptyList()),
        private val similarResult: Result<List<Movie>> = Result.success(emptyList())
    ) : MovieRepository {
        override suspend fun getPopularMovies(page: Int): Result<List<Movie>> = Result.success(emptyList())
        override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> = detailResult
        override suspend fun getMovieCredits(movieId: Int): Result<List<CastMember>> = creditsResult
        override suspend fun getSimilarMovies(movieId: Int, page: Int): Result<List<Movie>> = similarResult
    }

    @Test
    fun `use case emits combined movie detail, cast, and similar movies concurrently`() = runTest {
        val baseDetail = MovieDetail(
            id = 42,
            title = "Gladiator II",
            overview = "Epic tale...",
            posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
            releaseDate = "2024-11-15",
            rating = 8.1,
            runtime = 148,
            genres = listOf("Action", "Drama")
        )
        val expectedCast = listOf(
            CastMember(id = 1, name = "Paul Mescal", character = "Lucius", profileUrl = "")
        )
        val expectedSimilar = listOf(
            Movie(id = 2, title = "Gladiator", overview = "Original", posterUrl = "", releaseDate = "2000-05-05", rating = 8.5)
        )

        val repository = FakeMovieRepository(
            detailResult = Result.success(baseDetail),
            creditsResult = Result.success(expectedCast),
            similarResult = Result.success(expectedSimilar)
        )
        val useCase = GetMovieDetailUseCase(repository)

        val emission = useCase(movieId = 42).first()

        assertTrue(emission.isSuccess)
        val combined = emission.getOrNull()
        assertEquals(42, combined?.id)
        assertEquals(expectedCast, combined?.cast)
        assertEquals(expectedSimilar, combined?.similarMovies)
    }

    @Test
    fun `use case emits error when movie detail fails`() = runTest {
        val repository = FakeMovieRepository(Result.failure(RuntimeException("Not found")))
        val useCase = GetMovieDetailUseCase(repository)

        val emission = useCase(movieId = 999).first()

        assertTrue(emission.isFailure)
        assertEquals("Not found", emission.exceptionOrNull()?.message)
    }
}
