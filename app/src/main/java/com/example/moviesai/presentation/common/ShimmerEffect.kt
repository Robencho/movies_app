package com.example.moviesai.presentation.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviesai.ui.theme.MoviesAiTheme

@Composable
fun shimmerBrush(
    targetValue: Float = 1200f
): Brush {
    val shimmerColors = listOf(
        Color(0xFF1E1E1E),
        Color(0xFF2E2E2E),
        Color(0xFF1E1E1E)
    )

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}

@Composable
fun MovieCardShimmer(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        )
    }
}

@Composable
fun MovieGridShimmer(
    modifier: Modifier = Modifier
) {
    val brush = shimmerBrush()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = false
    ) {
        items(6) {
            MovieCardShimmer(brush = brush)
        }
    }
}

@Composable
fun MovieDetailShimmer(
    modifier: Modifier = Modifier
) {
    val brush = shimmerBrush()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Backdrop Shimmer (~350.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Title and meta shimmer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 60.dp, height = 20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(brush)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Genre chips shimmer
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(brush)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Synopsis title shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Synopsis body lines shimmer
            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(if (it == 3) 0.5f else 1f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cast row shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(4) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(36.dp))
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(65.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeShimmer(
    modifier: Modifier = Modifier
) {
    val brush = shimmerBrush()
    Column(modifier = modifier.fillMaxSize()) {
        // Banner Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Categories Shimmer
        repeat(2) {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                // Category title shimmer
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(width = 140.dp, height = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Row of cards shimmer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    repeat(3) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                            modifier = Modifier
                                .width(145.dp)
                                .aspectRatio(2f / 3f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(brush)
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
fun MovieGridShimmerPreview() {
    MoviesAiTheme {
        MovieGridShimmer()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun MovieDetailShimmerPreview() {
    MoviesAiTheme {
        MovieDetailShimmer()
    }
}
