package com.example.moviesai.presentation.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Favorites : Screen("favorites")
    data object PopularMovies : Screen("popular_movies")
    data object MovieDetail : Screen("movie_detail/{movieId}") {
        const val MOVIE_ID_ARG = "movieId"
        fun createRoute(movieId: Int): String = "movie_detail/$movieId"
    }
}
