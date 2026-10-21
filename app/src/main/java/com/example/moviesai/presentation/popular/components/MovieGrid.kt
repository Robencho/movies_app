@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.moviesai.presentation.popular.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun MovieGrid(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    onMovieClick: ((Movie) -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = movies,
            key = { movie -> movie.id }
        ) { movie ->
            MovieCard(
                movie = movie,
                onClick = onMovieClick?.let { { it(movie) } }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun MovieGridPreview() {
    MoviesAiTheme {
        MovieGrid(
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
                ),
                Movie(
                    id = 3,
                    title = "Deadpool & Wolverine",
                    overview = "Overview 3",
                    posterUrl = "",
                    releaseDate = "2024-07-26",
                    rating = 7.8
                )
            )
        )
    }
}
