package com.example.moviesai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CinemaColorScheme = darkColorScheme(
    primary = CinemaGold,
    onPrimary = CinemaBlack,
    primaryContainer = CinemaGoldContainer,
    onPrimaryContainer = CinemaGold,
    secondary = CinemaGold,
    onSecondary = CinemaBlack,
    secondaryContainer = CinemaSurfaceVariant,
    onSecondaryContainer = CinemaTextPrimary,
    tertiary = CinemaGoldDark,
    background = CinemaBlack,
    onBackground = CinemaTextPrimary,
    surface = CinemaDark,
    onSurface = CinemaTextPrimary,
    surfaceVariant = CinemaSurfaceVariant,
    onSurfaceVariant = CinemaTextSecondary,
    outline = CinemaBorder,
    error = CinemaError,
    onError = CinemaTextPrimary
)

@Composable
fun MoviesAiTheme(
    darkTheme: Boolean = true, // Force dark theme by default across the app
    dynamicColor: Boolean = false, // Keep cinematic streaming branding consistent
    content: @Composable () -> Unit
) {
    val colorScheme = CinemaColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = CinemaBlack.toArgb()
                window.navigationBarColor = CinemaBlack.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
