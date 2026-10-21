@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.moviesai.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesai.presentation.common.LocalAnimatedVisibilityScope
import com.example.moviesai.presentation.common.LocalSharedTransitionScope
import com.example.moviesai.presentation.detail.MovieDetailScreen
import com.example.moviesai.presentation.home.HomeScreen
import com.example.moviesai.presentation.main.MainScreen
import com.example.moviesai.presentation.popular.PopularMoviesScreen

@Composable
fun MoviesNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    SharedTransitionLayout(modifier = modifier) {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController = navController,
                startDestination = Screen.Main.route,
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing))
                }
            ) {
                composable(route = Screen.Main.route) {
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        MainScreen(
                            onMovieClick = { movie ->
                                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                            }
                        )
                    }
                }

                composable(route = Screen.Home.route) {
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        HomeScreen(
                            onMovieClick = { movie ->
                                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                            }
                        )
                    }
                }

                composable(route = Screen.PopularMovies.route) {
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        PopularMoviesScreen(
                            onMovieClick = { movie ->
                                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                            }
                        )
                    }
                }

                composable(
                    route = Screen.MovieDetail.route,
                    arguments = listOf(
                        navArgument(Screen.MovieDetail.MOVIE_ID_ARG) {
                            type = NavType.IntType
                        }
                    )
                ) {
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        MovieDetailScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onMovieClick = { similarMovie ->
                                navController.navigate(Screen.MovieDetail.createRoute(similarMovie.id))
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this
                        )
                    }
                }
            }
        }
    }
}
