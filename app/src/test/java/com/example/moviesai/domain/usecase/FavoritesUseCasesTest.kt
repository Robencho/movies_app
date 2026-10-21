package com.example.moviesai.domain.usecase

import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesUseCasesTest {

    private class FakeFavoritesRepository : MovieRepository {
        private val list = mutableListOf<Movie>()
        private val flow = MutableStateFlow<List<Movie>>(emptyList())

        override suspend fun getPopularMovies(page: Int) = Result.success(emptyList<Movie>())
        override suspend fun getMovieDetail(movieId: Int) = Result.failure<com.example.moviesai.domain.model.MovieDetail>(NotImplementedError())

        override fun getFavoriteMovies(): Flow<List<Movie>> = flow

        override suspend fun isFavorite(movieId: Int): Boolean = list.any { it.id == movieId }

        override suspend fun toggleFavorite(movie: Movie) {
            if (isFavorite(movie.id)) {
                list.removeAll { it.id == movie.id }
            } else {
                list.add(movie)
            }
            flow.value = list.toList()
        }
    }

    @Test
    fun `toggleFavorite adds and removes movie from favorites`() = runTest {
        val repo = FakeFavoritesRepository()
        val getFavoritesUseCase = GetFavoriteMoviesUseCase(repo)
        val isFavoriteUseCase = IsFavoriteUseCase(repo)
        val toggleFavoriteUseCase = ToggleFavoriteUseCase(repo)

        val movie = Movie(1, "Test Movie", "Overview", "", "2024", 8.0)

        assertFalse(isFavoriteUseCase(1))

        toggleFavoriteUseCase(movie)
        assertTrue(isFavoriteUseCase(1))
        assertEquals(1, getFavoritesUseCase().first().size)

        toggleFavoriteUseCase(movie)
        assertFalse(isFavoriteUseCase(1))
        assertEquals(0, getFavoritesUseCase().first().size)
    }
}
