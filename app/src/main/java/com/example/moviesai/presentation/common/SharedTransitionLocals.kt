@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.moviesai.presentation.common

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocals para propagar limpiamente los ámbitos de Shared Element Transitions
 * a través de la jerarquía de UI sin acoplar los parámetros de pantallas intermedias
 * y manteniendo la compatibilidad con @Preview.
 */
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * Curva de animación cinemática suave (400 ms con FastOutSlowInEasing) para las
 * transiciones compartidas de póster y título.
 */
val MovieSharedBoundsTransform = BoundsTransform { _, _ ->
    tween(durationMillis = 400, easing = FastOutSlowInEasing)
}
