package com.example.moviesai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.moviesai.presentation.navigation.MoviesNavHost
import com.example.moviesai.ui.theme.MoviesAiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAiTheme {
                MoviesNavHost()
            }
        }
    }
}
