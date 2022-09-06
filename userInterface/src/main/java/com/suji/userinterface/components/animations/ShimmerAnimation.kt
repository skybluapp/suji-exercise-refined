package com.suji.userinterface.components.animations

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Provides a brush that shimmers a composable
 * Used for placeholder UI to show a loading state
 * @param colors A list of colors that make up the shimmer
 */
@Composable
fun animatedShimmer(
    colors : List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )
): Brush {

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = Offset(
            x = translateAnim.value,
            y = translateAnim.value
        ),
    )
}

