package com.example.moviesai.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesai.domain.usecase.GetNowPlayingMoviesUseCase
import com.example.moviesai.domain.usecase.GetPopularMoviesUseCase
import com.example.moviesai.domain.usecase.GetTopRatedMoviesUseCase
import com.example.moviesai.domain.usecase.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeContent()
    }

    fun loadHomeContent() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val nowPlayingDeferred = async { getNowPlayingMoviesUseCase().first() }
            val popularDeferred = async { getPopularMoviesUseCase().first() }
            val topRatedDeferred = async { getTopRatedMoviesUseCase().first() }
            val upcomingDeferred = async { getUpcomingMoviesUseCase().first() }

            val nowPlayingResult = nowPlayingDeferred.await()
            val popularResult = popularDeferred.await()
            val topRatedResult = topRatedDeferred.await()
            val upcomingResult = upcomingDeferred.await()

            val nowPlaying = nowPlayingResult.getOrDefault(emptyList())
            val popular = popularResult.getOrDefault(emptyList())
            val topRated = topRatedResult.getOrDefault(emptyList())
            val upcoming = upcomingResult.getOrDefault(emptyList())

            if (nowPlaying.isEmpty() && popular.isEmpty() && topRated.isEmpty() && upcoming.isEmpty()) {
                val error = nowPlayingResult.exceptionOrNull()
                    ?: popularResult.exceptionOrNull()
                    ?: topRatedResult.exceptionOrNull()
                    ?: upcomingResult.exceptionOrNull()

                _uiState.value = HomeUiState.Error(
                    message = error?.localizedMessage
                        ?: "No se pudo cargar el contenido. Revisa tu conexión a internet."
                )
            } else {
                val featured = nowPlaying.firstOrNull() ?: popular.firstOrNull() ?: topRated.firstOrNull()
                _uiState.value = HomeUiState.Success(
                    content = HomeContent(
                        featuredMovie = featured,
                        nowPlaying = nowPlaying,
                        popular = popular,
                        topRated = topRated,
                        upcoming = upcoming
                    )
                )
            }
        }
    }

    fun retry() {
        loadHomeContent()
    }
}
