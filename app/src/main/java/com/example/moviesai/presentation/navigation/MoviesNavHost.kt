package com.example.moviesai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesai.presentation.detail.MovieDetailScreen
import com.example.moviesai.presentation.home.HomeScreen
import com.example.moviesai.presentation.main.MainScreen
import com.example.moviesai.presentation.popular.PopularMoviesScreen

@Composable
fun MoviesNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }
            )
        }

        composable(route = Screen.PopularMovies.route) {
            PopularMoviesScreen(
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }
            )
        }

        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(
                navArgument(Screen.MovieDetail.MOVIE_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) {
            MovieDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMovieClick = { similarMovie ->
                    navController.navigate(Screen.MovieDetail.createRoute(similarMovie.id))
                }
            )
        }
    }
}
