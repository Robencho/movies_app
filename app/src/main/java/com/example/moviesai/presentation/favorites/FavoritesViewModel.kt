package com.example.moviesai.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesai.domain.usecase.GetFavoriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase().collect { movies ->
                _uiState.value = if (movies.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Success(movies)
                }
            }
        }
    }
}
