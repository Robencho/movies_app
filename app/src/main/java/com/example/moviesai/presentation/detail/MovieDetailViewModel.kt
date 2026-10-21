package com.example.moviesai.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.domain.usecase.GetMovieDetailUseCase
import com.example.moviesai.domain.usecase.IsFavoriteUseCase
import com.example.moviesai.domain.usecase.ToggleFavoriteUseCase
import com.example.moviesai.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int? = savedStateHandle.get<Int>(Screen.MovieDetail.MOVIE_ID_ARG)
        ?: savedStateHandle.get<String>(Screen.MovieDetail.MOVIE_ID_ARG)?.toIntOrNull()

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        loadMovieDetail()
    }

    fun loadMovieDetail() {
        val id = movieId
        if (id == null || id <= 0) {
            _uiState.value = MovieDetailUiState.Error("Identificador de película inválido o no encontrado.")
            return
        }

        viewModelScope.launch {
            _uiState.value = MovieDetailUiState.Loading
            checkFavoriteStatus(id)
            getMovieDetailUseCase(id).collect { result ->
                result.fold(
                    onSuccess = { movieDetail ->
                        _uiState.value = MovieDetailUiState.Success(movieDetail)
                    },
                    onFailure = { error ->
                        _uiState.value = MovieDetailUiState.Error(
                            message = error.localizedMessage ?: "Ocurrió un error al cargar el detalle de la película."
                        )
                    }
                )
            }
        }
    }

    private fun checkFavoriteStatus(id: Int) {
        viewModelScope.launch {
            _isFavorite.value = isFavoriteUseCase(id)
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavoriteUseCase(movie)
            _isFavorite.value = !_isFavorite.value
        }
    }

    fun retry() {
        loadMovieDetail()
    }
}
