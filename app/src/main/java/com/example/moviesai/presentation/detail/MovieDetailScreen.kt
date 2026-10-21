@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.moviesai.presentation.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesai.data.mapper.toMovie
import com.example.moviesai.domain.model.CastMember
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.domain.model.MovieDetail
import com.example.moviesai.presentation.common.LocalAnimatedVisibilityScope
import com.example.moviesai.presentation.common.LocalSharedTransitionScope
import com.example.moviesai.presentation.common.MovieDetailShimmer
import com.example.moviesai.presentation.common.MovieSharedBoundsTransform
import com.example.moviesai.presentation.popular.components.MovieCard
import com.example.moviesai.ui.theme.CinemaBlack
import com.example.moviesai.ui.theme.CinemaBorder
import com.example.moviesai.ui.theme.CinemaDark
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaSurfaceVariant
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary
import com.example.moviesai.ui.theme.CinemaTextTertiary
import com.example.moviesai.ui.theme.MoviesAiTheme
import java.util.Locale

@Composable
fun MovieDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onMovieClick: ((Movie) -> Unit)? = null,
    sharedTransitionScope: SharedTransitionScope? = LocalSharedTransitionScope.current,
    animatedVisibilityScope: AnimatedVisibilityScope? = LocalAnimatedVisibilityScope.current,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    MovieDetailContent(
        uiState = uiState,
        isFavorite = isFavorite,
        onBackClick = onBackClick,
        onToggleFavorite = { movie -> viewModel.toggleFavorite(movie) },
        onRetry = { viewModel.retry() },
        onMovieClick = onMovieClick,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailContent(
    uiState: MovieDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: ((Movie) -> Unit)? = null,
    onMovieClick: ((Movie) -> Unit)? = null,
    sharedTransitionScope: SharedTransitionScope? = LocalSharedTransitionScope.current,
    animatedVisibilityScope: AnimatedVisibilityScope? = LocalAnimatedVisibilityScope.current
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
                is MovieDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MovieDetailShimmer(modifier = Modifier.fillMaxSize())
                        // Floating back button
                        GlassmorphicBackButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(start = 16.dp, top = 12.dp)
                                .align(Alignment.TopStart)
                        )
                    }
                }

                is MovieDetailUiState.Error -> {
                    DetailErrorContent(
                        message = uiState.message,
                        onBackClick = onBackClick,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is MovieDetailUiState.Success -> {
                    val movie = uiState.movie
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // 1. Immersive Backdrop Header (~350.dp) with Gradient Fade & Floating Poster
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                        ) {
                            // Backdrop Image
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(movie.backdropUrl.ifBlank { movie.posterUrl.ifBlank { null } })
                                    .crossfade(true)
                                    .placeholder(android.R.drawable.ic_menu_gallery)
                                    .error(android.R.drawable.stat_notify_error)
                                    .build(),
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            )

                            // Smooth vertical gradient fade into background color
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colorStops = arrayOf(
                                                0.0f to Color.Black.copy(alpha = 0.45f),
                                                0.35f to Color.Transparent,
                                                0.7f to CinemaBlack.copy(alpha = 0.75f),
                                                1.0f to CinemaBlack
                                            )
                                        )
                                    )
                            )

                            // Floating Glassmorphic Back Button
                            GlassmorphicBackButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(start = 16.dp, top = 12.dp)
                                    .align(Alignment.TopStart)
                            )

                            // Floating Glassmorphic Favorite Button
                            GlassmorphicFavoriteButton(
                                isFavorite = isFavorite,
                                onClick = { onToggleFavorite?.invoke(movie.toMovie()) },
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(end = 16.dp, top = 12.dp)
                                    .align(Alignment.TopEnd)
                            )

                            // Shared Element Modifiers for Poster & Title
                            val posterSharedModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                                with(sharedTransitionScope) {
                                    Modifier.sharedElement(
                                        state = rememberSharedContentState(key = "movie_poster_${movie.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = MovieSharedBoundsTransform
                                    )
                                }
                            } else {
                                Modifier
                            }

                            val titleSharedModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                                with(sharedTransitionScope) {
                                    Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "movie_title_${movie.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = MovieSharedBoundsTransform
                                    )
                                }
                            } else {
                                Modifier
                            }

                            // Floating Poster overlapping the bottom of the backdrop header
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                colors = CardDefaults.cardColors(containerColor = CinemaDark),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 16.dp)
                                    .width(120.dp)
                                    .aspectRatio(2f / 3f)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(movie.posterUrl.ifBlank { null })
                                        .crossfade(true)
                                        .placeholder(android.R.drawable.ic_menu_gallery)
                                        .error(android.R.drawable.stat_notify_error)
                                        .build(),
                                    contentDescription = movie.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .then(posterSharedModifier)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }

                            // Movie Title & Metadata next to floating poster
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 152.dp, end = 16.dp, bottom = 8.dp)
                            ) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CinemaTextPrimary,
                                    maxLines = 3,
                                    modifier = titleSharedModifier
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Rating Badge
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF262100))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating",
                                            tint = CinemaGold,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = String.format(Locale.getDefault(), "%.1f", movie.rating),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = CinemaGold
                                        )
                                    }

                                    if (movie.runtime > 0) {
                                        Text(
                                            text = formatRuntime(movie.runtime),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = CinemaTextSecondary
                                        )
                                    }
                                }

                                if (movie.releaseDate.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Estreno: ${movie.releaseDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CinemaTextTertiary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 2. Overlapping Dark Surface Card with Genres & Synopsis
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CinemaDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                // Genre Chips
                                if (movie.genres.isNotEmpty()) {
                                    Text(
                                        text = "Géneros",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = CinemaGold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        movie.genres.forEach { genre ->
                                            SuggestionChip(
                                                onClick = {},
                                                label = {
                                                    Text(
                                                        text = genre,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                },
                                                colors = SuggestionChipDefaults.suggestionChipColors(
                                                    containerColor = CinemaSurfaceVariant,
                                                    labelColor = CinemaTextPrimary
                                                ),
                                                border = SuggestionChipDefaults.suggestionChipBorder(
                                                    enabled = true,
                                                    borderColor = CinemaBorder
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                // Overview Section
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 3.dp, height = 18.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(CinemaGold)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Sinopsis",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = CinemaTextPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (movie.overview.isNotBlank()) {
                                        movie.overview
                                    } else {
                                        "No hay sinopsis disponible para esta película."
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 24.sp,
                                    color = CinemaTextSecondary
                                )
                            }
                        }

                        // 3. Reparto Principal (Cast Section)
                        if (movie.cast.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastSection(cast = movie.cast)
                        }

                        // 4. Películas Similares (Similar Movies Section)
                        if (movie.similarMovies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            SimilarMoviesSection(
                                similarMovies = movie.similarMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CastSection(
    cast: List<CastMember>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CinemaGold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Reparto Principal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CinemaTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = cast,
                key = { member -> member.id }
            ) { member ->
                CastMemberItem(member = member)
            }
        }
    }
}

@Composable
fun CastMemberItem(
    member: CastMember,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(88.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(member.profileUrl.ifBlank { null })
                .crossfade(true)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .build(),
            contentDescription = member.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(1.5.dp, CinemaBorder, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = member.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = CinemaTextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        if (member.character.isNotBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = member.character,
                style = MaterialTheme.typography.labelSmall,
                color = CinemaTextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SimilarMoviesSection(
    similarMovies: List<Movie>,
    onMovieClick: ((Movie) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CinemaGold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Películas Similares",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CinemaTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = similarMovies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onMovieClick?.invoke(movie) },
                    modifier = Modifier.width(140.dp)
                )
            }
        }
    }
}

@Composable
fun GlassmorphicBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Regresar",
            tint = Color.White
        )
    }
}

@Composable
fun GlassmorphicFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
            tint = if (isFavorite) Color(0xFFE53935) else Color.White
        )
    }
}

private fun formatRuntime(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return if (hours > 0) {
        "${hours}h ${remainingMinutes}m"
    } else {
        "${minutes} min"
    }
}

@Composable
fun DetailErrorContent(
    message: String,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        GlassmorphicBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 12.dp)
                .align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
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
                text = "Error al cargar",
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
fun MovieDetailScreenLoadingPreview() {
    MoviesAiTheme {
        MovieDetailContent(
            uiState = MovieDetailUiState.Loading,
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun MovieDetailScreenSuccessPreview() {
    MoviesAiTheme {
        MovieDetailContent(
            uiState = MovieDetailUiState.Success(
                movie = MovieDetail(
                    id = 1,
                    title = "Dune: Part Two",
                    overview = "Sigue el viaje mítico de Paul Atreides mientras se une a Chani y los Fremen en una guerra de venganza contra los conspiradores que destruyeron a su familia.",
                    posterUrl = "",
                    backdropUrl = "",
                    releaseDate = "2024-03-01",
                    rating = 8.3,
                    runtime = 166,
                    genres = listOf("Ciencia ficción", "Aventura", "Acción"),
                    cast = listOf(
                        CastMember(1, "Timothée Chalamet", "Paul Atreides", ""),
                        CastMember(2, "Zendaya", "Chani", ""),
                        CastMember(3, "Rebecca Ferguson", "Lady Jessica", "")
                    ),
                    similarMovies = listOf(
                        Movie(2, "Blade Runner 2049", "Overview...", "", "2017-10-06", 8.0),
                        Movie(3, "Interstellar", "Overview...", "", "2014-11-07", 8.7)
                    )
                )
            ),
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun MovieDetailScreenErrorPreview() {
    MoviesAiTheme {
        MovieDetailContent(
            uiState = MovieDetailUiState.Error(
                message = "No se pudieron obtener los detalles de la película. Verifica tu conexión a internet."
            ),
            onBackClick = {},
            onRetry = {}
        )
    }
}
