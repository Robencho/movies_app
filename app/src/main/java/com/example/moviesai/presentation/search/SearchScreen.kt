package com.example.moviesai.presentation.search

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.moviesai.ui.theme.CinemaBorder
import com.example.moviesai.ui.theme.CinemaDark
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaSurface
import com.example.moviesai.ui.theme.CinemaSurfaceVariant
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun SearchScreen(
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchContent(
        query = query,
        uiState = uiState,
        onQueryChange = { viewModel.onQueryChanged(it) },
        onClearQuery = { viewModel.onQueryChanged("") },
        onRetry = { viewModel.retry() },
        onMovieClick = onMovieClick,
        modifier = modifier
    )
}

@Composable
fun SearchContent(
    query: String,
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onRetry: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = CinemaBlack,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CinemaBlack)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Search TextField
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = {
                        Text(
                            text = "Buscar películas, series...",
                            color = CinemaTextSecondary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = CinemaGold
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar",
                                    tint = CinemaTextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CinemaDark,
                        unfocusedContainerColor = CinemaDark,
                        disabledContainerColor = CinemaDark,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = CinemaGold,
                        focusedTextColor = CinemaTextPrimary,
                        unfocusedTextColor = CinemaTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CinemaBorder, RoundedCornerShape(16.dp))
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is SearchUiState.Idle -> {
                    SearchMessageView(
                        icon = Icons.Default.Search,
                        title = "Explora el catálogo",
                        subtitle = "Escribe el nombre de tu película favorita para encontrarla en segundos."
                    )
                }

                is SearchUiState.Loading -> {
                    MovieGridShimmer(modifier = Modifier.fillMaxSize())
                }

                is SearchUiState.Empty -> {
                    SearchMessageView(
                        icon = Icons.Default.Search,
                        title = "Sin resultados",
                        subtitle = "No encontramos películas que coincidan con \"$query\"."
                    )
                }

                is SearchUiState.Success -> {
                    MovieGrid(
                        movies = uiState.movies,
                        onMovieClick = onMovieClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is SearchUiState.Error -> {
                    SearchErrorView(
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
fun SearchMessageView(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize().padding(32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CinemaSurface)
                    .border(1.dp, CinemaBorder, CircleShape)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CinemaGold,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CinemaTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = CinemaTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SearchErrorView(
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
                text = "Error en la búsqueda",
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
fun SearchScreenIdlePreview() {
    MoviesAiTheme {
        SearchContent(
            query = "",
            uiState = SearchUiState.Idle,
            onQueryChange = {},
            onClearQuery = {},
            onRetry = {},
            onMovieClick = {}
        )
    }
}
