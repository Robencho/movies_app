package com.example.moviesai.presentation.popular

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.presentation.common.MovieGridShimmer
import com.example.moviesai.presentation.popular.components.MovieGrid
import com.example.moviesai.ui.theme.CinemaBlack
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun PopularMoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: PopularMoviesViewModel = hiltViewModel(),
    onMovieClick: ((Movie) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PopularMoviesContent(
        uiState = uiState,
        onRetry = { viewModel.retry() },
        onMovieClick = onMovieClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularMoviesContent(
    uiState: PopularMoviesUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onMovieClick: ((Movie) -> Unit)? = null
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 4.dp, height = 20.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(CinemaGold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Películas Populares",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = CinemaTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CinemaBlack,
                    titleContentColor = CinemaTextPrimary
                )
            )
        },
        containerColor = CinemaBlack,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is PopularMoviesUiState.Loading -> {
                    MovieGridShimmer(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is PopularMoviesUiState.Success -> {
                    MovieGrid(
                        movies = uiState.movies,
                        onMovieClick = onMovieClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is PopularMoviesUiState.Error -> {
                    ErrorContent(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Algo salió mal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CinemaTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = CinemaTextSecondary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaGold,
                    contentColor = CinemaBlack
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Reintentar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PopularMoviesScreenLoadingPreview() {
    MoviesAiTheme {
        PopularMoviesContent(
            uiState = PopularMoviesUiState.Loading,
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PopularMoviesScreenSuccessPreview() {
    MoviesAiTheme {
        PopularMoviesContent(
            uiState = PopularMoviesUiState.Success(
                movies = listOf(
                    Movie(
                        id = 1,
                        title = "Dune: Part Two",
                        overview = "Overview 1",
                        posterUrl = "",
                        releaseDate = "2024-03-01",
                        rating = 8.3
                    ),
                    Movie(
                        id = 2,
                        title = "Inside Out 2",
                        overview = "Overview 2",
                        posterUrl = "",
                        releaseDate = "2024-06-14",
                        rating = 7.9
                    )
                )
            ),
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun PopularMoviesScreenErrorPreview() {
    MoviesAiTheme {
        PopularMoviesContent(
            uiState = PopularMoviesUiState.Error(
                message = "No se pudo conectar con el servidor. Revisa tu conexión a internet."
            ),
            onRetry = {}
        )
    }
}
