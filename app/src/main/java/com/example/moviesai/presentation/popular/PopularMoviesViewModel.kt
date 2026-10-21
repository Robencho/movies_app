package com.example.moviesai.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesai.domain.usecase.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PopularMoviesUiState>(PopularMoviesUiState.Loading)
    val uiState: StateFlow<PopularMoviesUiState> = _uiState.asStateFlow()

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = PopularMoviesUiState.Loading
            getPopularMoviesUseCase(page).collect { result ->
                result.fold(
                    onSuccess = { movies ->
                        _uiState.value = PopularMoviesUiState.Success(movies)
                    },
                    onFailure = { error ->
                        _uiState.value = PopularMoviesUiState.Error(
                            message = error.localizedMessage ?: "Ocurrió un error inesperado al cargar las películas."
                        )
                    }
                )
            }
        }
    }

    fun retry() {
        loadPopularMovies()
    }
}
