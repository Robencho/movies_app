package com.example.moviesai.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesai.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        _query
            .debounce(400)
            .distinctUntilChanged()
            .onEach { q ->
                if (q.isBlank()) {
                    _uiState.value = SearchUiState.Idle
                } else {
                    executeSearch(q)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            _uiState.value = SearchUiState.Idle
        }
    }

    private fun executeSearch(searchQuery: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            searchMoviesUseCase(searchQuery).collect { result ->
                result.fold(
                    onSuccess = { movies ->
                        _uiState.value = if (movies.isEmpty()) {
                            SearchUiState.Empty
                        } else {
                            SearchUiState.Success(movies)
                        }
                    },
                    onFailure = { error ->
                        _uiState.value = SearchUiState.Error(
                            error.localizedMessage ?: "Ocurrió un error al realizar la búsqueda."
                        )
                    }
                )
            }
        }
    }

    fun retry() {
        val current = _query.value
        if (current.isNotBlank()) {
            executeSearch(current)
        }
    }
}
