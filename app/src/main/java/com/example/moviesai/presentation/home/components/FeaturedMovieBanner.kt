package com.example.moviesai.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.ui.theme.CinemaBlack
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary
import com.example.moviesai.ui.theme.MoviesAiTheme
import java.util.Locale

@Composable
fun FeaturedMovieBanner(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(430.dp)
            .clickable { onMovieClick(movie) }
    ) {
        // High-res Backdrop / Poster
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.backdropUrl.ifBlank { movie.posterUrl.ifBlank { null } })
                .crossfade(true)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Multi-directional gradient for legibility & seamless integration into dark theme
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to CinemaBlack.copy(alpha = 0.65f),
                            0.25f to Color.Transparent,
                            0.65f to CinemaBlack.copy(alpha = 0.7f),
                            0.95f to CinemaBlack,
                            1.0f to CinemaBlack
                        )
                    )
                )
        )

        // Banner content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Golden Tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CinemaGold.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(CinemaGold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "DESTACADA HOY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = CinemaGold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Movie Title
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = CinemaTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Rating & Release
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = CinemaGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", movie.rating),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = CinemaGold
                    )
                }

                if (movie.releaseDate.isNotBlank()) {
                    Text(
                        text = movie.releaseDate.take(4),
                        style = MaterialTheme.typography.bodyMedium,
                        color = CinemaTextSecondary
                    )
                }
            }

            if (movie.overview.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    color = CinemaTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CTA Button "Ver Detalle"
            Button(
                onClick = { onMovieClick(movie) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaGold,
                    contentColor = CinemaBlack
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ver Detalle",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun FeaturedMovieBannerPreview() {
    MoviesAiTheme {
        FeaturedMovieBanner(
            movie = Movie(
                id = 1,
                title = "Dune: Parte Dos",
                overview = "Paul Atreides se une a Chani y a los Fremen mientras busca venganza contra los conspiradores.",
                posterUrl = "",
                backdropUrl = "",
                releaseDate = "2024-03-01",
                rating = 8.3
            ),
            onMovieClick = {}
        )
    }
}
