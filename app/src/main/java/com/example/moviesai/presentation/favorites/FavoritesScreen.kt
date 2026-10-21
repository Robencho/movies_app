package com.example.moviesai.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import com.example.moviesai.ui.theme.CinemaBorder
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaSurface
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary
import com.example.moviesai.ui.theme.CinemaTextTertiary
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun FavoritesScreen(
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesContent(
        uiState = uiState,
        onMovieClick = onMovieClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesContent(
    uiState: FavoritesUiState,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
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
                            text = "Mis Favoritos",
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
                is FavoritesUiState.Loading -> {
                    MovieGridShimmer(modifier = Modifier.fillMaxSize())
                }

                is FavoritesUiState.Empty -> {
                    FavoritesEmptyContent(modifier = Modifier.fillMaxSize())
                }

                is FavoritesUiState.Success -> {
                    MovieGrid(
                        movies = uiState.movies,
                        onMovieClick = onMovieClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesEmptyContent(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(CinemaSurface)
                    .border(1.5.dp, CinemaBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = CinemaGold,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "No tienes películas favoritas aún",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CinemaTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Guarda tus películas preferidas tocando el corazón en la pantalla de detalle para verlas aquí sin conexión.",
                style = MaterialTheme.typography.bodyMedium,
                color = CinemaTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun FavoritesScreenEmptyPreview() {
    MoviesAiTheme {
        FavoritesContent(
            uiState = FavoritesUiState.Empty,
            onMovieClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun FavoritesScreenSuccessPreview() {
    MoviesAiTheme {
        FavoritesContent(
            uiState = FavoritesUiState.Success(
                movies = listOf(
                    Movie(
                        id = 1,
                        title = "Dune: Part Two",
                        overview = "Overview 1",
                        posterUrl = "",
                        releaseDate = "2024-03-01",
                        rating = 8.3
                    )
                )
            ),
            onMovieClick = {}
        )
    }
}
