package com.example.moviesai.presentation.popular.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.moviesai.ui.theme.MoviesAiTheme
import java.util.Locale

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF161616)
        ),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Poster Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl.ifBlank { null })
                    .crossfade(true)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Smooth vertical gradient overlay at the bottom for readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CinemaBlack.copy(alpha = 0.6f),
                                CinemaBlack.copy(alpha = 0.95f)
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = CinemaGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = String.format(Locale.getDefault(), "%.1f", movie.rating),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        if (movie.releaseDate.isNotBlank()) {
                            Text(
                                text = movie.releaseDate.take(4),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun MovieCardPreview() {
    MoviesAiTheme {
        MovieCard(
            movie = Movie(
                id = 1,
                title = "Dune: Part Two",
                overview = "Paul Atreides unites with Chani and the Fremen while seeking revenge...",
                posterUrl = "",
                releaseDate = "2024-03-01",
                rating = 8.3
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
