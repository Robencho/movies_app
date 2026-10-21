package com.example.moviesai.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.domain.repository.MovieRepository
import com.example.moviesai.domain.usecase.GetMovieDetailUseCase
import com.example.moviesai.domain.usecase.IsFavoriteUseCase
import com.example.moviesai.domain.usecase.ToggleFavoriteUseCase
import com.example.moviesai.presentation.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeMovieRepository(
        private val detailResult: Result<MovieDetail>,
        private var isFav: Boolean = false
    ) : MovieRepository {
        override suspend fun getPopularMovies(page: Int): Result<List<Movie>> = Result.success(emptyList())
        override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> = detailResult
        override suspend fun isFavorite(movieId: Int): Boolean = isFav
        override suspend fun toggleFavorite(movie: Movie) {
            isFav = !isFav
        }
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
    fun `viewModel emits Success state and tracks favorite when use case succeeds`() = runTest(testDispatcher) {
        val sampleMovie = MovieDetail(
            id = 42,
            title = "Test Movie",
            overview = "Overview",
            posterUrl = "",
            backdropUrl = "",
            releaseDate = "2024-01-01",
            rating = 8.0,
            runtime = 120,
            genres = listOf("Action")
        )
        val repository = FakeMovieRepository(Result.success(sampleMovie), isFav = true)
        val useCase = GetMovieDetailUseCase(repository)
        val isFavUseCase = IsFavoriteUseCase(repository)
        val toggleFavUseCase = ToggleFavoriteUseCase(repository)
        val savedStateHandle = SavedStateHandle(mapOf(Screen.MovieDetail.MOVIE_ID_ARG to 42))

        val viewModel = MovieDetailViewModel(useCase, isFavUseCase, toggleFavUseCase, savedStateHandle)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is MovieDetailUiState.Success)
        val state = viewModel.uiState.value as MovieDetailUiState.Success
        assertEquals("Test Movie", state.movie.title)
        assertTrue(viewModel.isFavorite.value)

        viewModel.toggleFavorite(Movie(42, "Test Movie", "", "", "", 8.0))
        advanceUntilIdle()
        assertFalse(viewModel.isFavorite.value)
    }

    @Test
    fun `viewModel emits Error state when movieId is invalid`() = runTest(testDispatcher) {
        val repository = FakeMovieRepository(Result.failure(RuntimeException("Error")))
        val useCase = GetMovieDetailUseCase(repository)
        val isFavUseCase = IsFavoriteUseCase(repository)
        val toggleFavUseCase = ToggleFavoriteUseCase(repository)
        val savedStateHandle = SavedStateHandle(emptyMap())

        val viewModel = MovieDetailViewModel(useCase, isFavUseCase, toggleFavUseCase, savedStateHandle)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is MovieDetailUiState.Error)
    }

    @Test
    fun `viewModel emits Error state when use case fails`() = runTest(testDispatcher) {
        val repository = FakeMovieRepository(Result.failure(RuntimeException("Network failure")))
        val useCase = GetMovieDetailUseCase(repository)
        val isFavUseCase = IsFavoriteUseCase(repository)
        val toggleFavUseCase = ToggleFavoriteUseCase(repository)
        val savedStateHandle = SavedStateHandle(mapOf(Screen.MovieDetail.MOVIE_ID_ARG to 42))

        val viewModel = MovieDetailViewModel(useCase, isFavUseCase, toggleFavUseCase, savedStateHandle)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is MovieDetailUiState.Error)
        val state = viewModel.uiState.value as MovieDetailUiState.Error
        assertEquals("Network failure", state.message)
    }
}
