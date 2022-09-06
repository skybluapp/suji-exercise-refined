package com.suji.userinterface.components.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

/**
 * Filter that can be put over Composables to darken content behind
 * @param visible is the filter applied
 * @param onClick runs a function if the filter is clicked
 */
@Composable
fun DarkFilter(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ),
        exit = fadeOut(
            tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .alpha(0.8f)
                .background(color = Color.Black)
                .clickable {
                    onClick()
                }
        )
    }
}