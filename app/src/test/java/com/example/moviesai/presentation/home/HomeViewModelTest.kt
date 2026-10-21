package com.example.moviesai.presentation.home

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import com.example.moviesai.domain.usecase.GetNowPlayingMoviesUseCase
import com.example.moviesai.domain.usecase.GetPopularMoviesUseCase
import com.example.moviesai.domain.usecase.GetTopRatedMoviesUseCase
import com.example.moviesai.domain.usecase.GetUpcomingMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeMovieRepository(
        private val shouldFail: Boolean = false
    ) : MovieRepository {
        val testMovie = Movie(
            id = 1,
            title = "Movie 1",
            overview = "Overview",
            posterUrl = "",
            backdropUrl = "",
            releaseDate = "2024-01-01",
            rating = 8.0
        )

        override suspend fun getPopularMovies(page: Int): Result<List<Movie>> =
            if (shouldFail) Result.failure(IOException("Fail")) else Result.success(listOf(testMovie.copy(title = "Popular")))

        override suspend fun getNowPlayingMovies(page: Int): Result<List<Movie>> =
            if (shouldFail) Result.failure(IOException("Fail")) else Result.success(listOf(testMovie.copy(title = "Now Playing")))

        override suspend fun getUpcomingMovies(page: Int): Result<List<Movie>> =
            if (shouldFail) Result.failure(IOException("Fail")) else Result.success(listOf(testMovie.copy(title = "Upcoming")))

        override suspend fun getTopRatedMovies(page: Int): Result<List<Movie>> =
            if (shouldFail) Result.failure(IOException("Fail")) else Result.success(listOf(testMovie.copy(title = "Top Rated")))

        override suspend fun getMovieDetail(movieId: Int) =
            Result.failure<com.example.moviesai.domain.model.MovieDetail>(NotImplementedError())
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel loads all categories asynchronously and sets Success state`() = runTest(testDispatcher) {
        val repo = FakeMovieRepository(shouldFail = false)
        val viewModel = HomeViewModel(
            getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCase(repo),
            getPopularMoviesUseCase = GetPopularMoviesUseCase(repo),
            getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase(repo),
            getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(repo)
        )

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is HomeUiState.Success)
        val successState = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, successState.content.nowPlaying.size)
        assertEquals(1, successState.content.popular.size)
        assertEquals(1, successState.content.topRated.size)
        assertEquals(1, successState.content.upcoming.size)
        assertEquals("Now Playing", successState.content.featuredMovie?.title)
    }

    @Test
    fun `viewModel sets Error state when all categories fail`() = runTest(testDispatcher) {
        val repo = FakeMovieRepository(shouldFail = true)
        val viewModel = HomeViewModel(
            getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCase(repo),
            getPopularMoviesUseCase = GetPopularMoviesUseCase(repo),
            getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase(repo),
            getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(repo)
        )

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is HomeUiState.Error)
    }
}
