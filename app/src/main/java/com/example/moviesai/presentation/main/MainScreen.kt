package com.example.moviesai.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moviesai.domain.model.Movie
import com.example.moviesai.presentation.favorites.FavoritesScreen
import com.example.moviesai.presentation.home.HomeScreen
import com.example.moviesai.presentation.search.SearchScreen
import com.example.moviesai.ui.theme.CinemaBlack
import com.example.moviesai.ui.theme.CinemaDark
import com.example.moviesai.ui.theme.CinemaGold
import com.example.moviesai.ui.theme.CinemaTextPrimary
import com.example.moviesai.ui.theme.CinemaTextSecondary

enum class NavigationItem(val label: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Default.Home),
    SEARCH("Buscar", Icons.Default.Search),
    FAVORITES("Favoritos", Icons.Default.Favorite)
}

@Composable
fun MainScreen(
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by rememberSaveable { mutableStateOf(NavigationItem.HOME) }

    Scaffold(
        containerColor = CinemaBlack,
        bottomBar = {
            NavigationBar(
                containerColor = CinemaDark,
                tonalElevation = 8.dp
            ) {
                NavigationItem.entries.forEach { item ->
                    val selected = item == selectedItem
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedItem = item },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) CinemaGold else CinemaTextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CinemaGold,
                            unselectedIconColor = CinemaTextSecondary,
                            indicatorColor = CinemaGold.copy(alpha = 0.18f)
                        )
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                NavigationItem.HOME -> HomeScreen(onMovieClick = onMovieClick)
                NavigationItem.SEARCH -> SearchScreen(onMovieClick = onMovieClick)
                NavigationItem.FAVORITES -> FavoritesScreen(onMovieClick = onMovieClick)
            }
        }
    }
}
