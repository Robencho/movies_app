package com.example.moviesai.presentation.favorites

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import com.example.moviesai.domain.usecase.GetFavoriteMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeMovieRepository(
        private val favoritesFlow: Flow<List<Movie>>
    ) : MovieRepository {
        override suspend fun getPopularMovies(page: Int) = Result.success(emptyList<Movie>())
        override suspend fun getMovieDetail(movieId: Int) = Result.failure<com.example.moviesai.domain.model.MovieDetail>(NotImplementedError())
        override fun getFavoriteMovies(): Flow<List<Movie>> = favoritesFlow
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
    fun `viewModel emits Empty state when favorites list is empty`() = runTest(testDispatcher) {
        val flow = MutableStateFlow<List<Movie>>(emptyList())
        val repository = FakeMovieRepository(flow)
        val viewModel = FavoritesViewModel(GetFavoriteMoviesUseCase(repository))

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is FavoritesUiState.Empty)
    }

    @Test
    fun `viewModel emits Success state when favorites exist`() = runTest(testDispatcher) {
        val sampleMovie = Movie(1, "Fav Movie", "Overview", "", "2024", 8.0)
        val flow = MutableStateFlow<List<Movie>>(listOf(sampleMovie))
        val repository = FakeMovieRepository(flow)
        val viewModel = FavoritesViewModel(GetFavoriteMoviesUseCase(repository))

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is FavoritesUiState.Success)
        val state = viewModel.uiState.value as FavoritesUiState.Success
        assertEquals(1, state.movies.size)
        assertEquals("Fav Movie", state.movies[0].title)
    }
}
