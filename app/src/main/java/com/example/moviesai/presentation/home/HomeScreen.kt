package com.example.moviesai.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.presentation.common.HomeShimmer
import com.example.moviesai.presentation.home.components.FeaturedMovieBanner
import com.example.moviesai.presentation.home.components.MovieCategoryRow
import com.example.moviesai.ui.theme.CinemaBlack
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onRetry = { viewModel.retry() },
        onMovieClick = onMovieClick,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = CinemaBlack,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    HomeShimmer(modifier = Modifier.fillMaxSize())
                }

                is HomeUiState.Error -> {
                    HomeErrorContent(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is HomeUiState.Success -> {
                    val content = uiState.content
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // 1. Featured Header Carousel / Banner
                        if (content.featuredMovie != null) {
                            FeaturedMovieBanner(
                                movie = content.featuredMovie,
                                onMovieClick = onMovieClick
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 2. Horizontal Movie Carousels for each category
                        MovieCategoryRow(
                            title = "En Cines",
                            movies = content.nowPlaying,
                            onMovieClick = onMovieClick
                        )

                        MovieCategoryRow(
                            title = "Populares",
                            movies = content.popular,
                            onMovieClick = onMovieClick
                        )

                        MovieCategoryRow(
                            title = "Mejor Valoradas",
                            movies = content.topRated,
                            onMovieClick = onMovieClick
                        )

                        MovieCategoryRow(
                            title = "Próximamente",
                            movies = content.upcoming,
                            onMovieClick = onMovieClick
                        )

                        Spacer(modifier = Modifier.height(36.dp))
                    }

                    // Floating Streaming Top Header with App Brand
                    HomeTopBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CinemaBlack.copy(alpha = 0.85f),
                        CinemaBlack.copy(alpha = 0.4f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 22.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CinemaGold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MOVIES AI",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                color = CinemaTextPrimary
            )
        }
    }
}

@Composable
fun HomeErrorContent(
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
                text = "No se pudo cargar el contenido",
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
fun HomeScreenLoadingPreview() {
    MoviesAiTheme {
        HomeContent(
            uiState = HomeUiState.Loading,
            onRetry = {},
            onMovieClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun HomeScreenSuccessPreview() {
    val sampleMovies = listOf(
        Movie(
            id = 1,
            title = "Dune: Part Two",
            overview = "Paul Atreides se une a Chani y los Fremen en busca de venganza.",
            posterUrl = "",
            backdropUrl = "",
            releaseDate = "2024-03-01",
            rating = 8.3
        ),
        Movie(
            id = 2,
            title = "Inside Out 2",
            overview = "Nuevas emociones llegan a la mente de Riley.",
            posterUrl = "",
            backdropUrl = "",
            releaseDate = "2024-06-14",
            rating = 7.9
        )
    )

    MoviesAiTheme {
        HomeContent(
            uiState = HomeUiState.Success(
                content = HomeContent(
                    featuredMovie = sampleMovies.first(),
                    nowPlaying = sampleMovies,
                    popular = sampleMovies,
                    topRated = sampleMovies,
                    upcoming = sampleMovies
                )
            ),
            onRetry = {},
            onMovieClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun HomeScreenErrorPreview() {
    MoviesAiTheme {
        HomeContent(
            uiState = HomeUiState.Error(
                message = "Error al conectar con los servidores de TMDb. Revisa tu conexión."
            ),
            onRetry = {},
            onMovieClick = {}
        )
    }
}
