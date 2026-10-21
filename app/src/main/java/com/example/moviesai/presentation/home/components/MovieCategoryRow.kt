package com.example.moviesai.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.presentation.popular.components.MovieCard
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun MovieCategoryRow(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Section Header
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
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CinemaTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Carousel
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = movies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie) },
                    modifier = Modifier.width(145.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun MovieCategoryRowPreview() {
    MoviesAiTheme {
        MovieCategoryRow(
            title = "En Cines",
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
            ),
            onMovieClick = {}
        )
    }
}
