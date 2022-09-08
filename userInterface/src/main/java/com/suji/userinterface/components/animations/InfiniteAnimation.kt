package com.suji.userinterface.components.animations

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import timber.log.Timber

/**
 * Runs an animated vector on a loop
 * @param animatedVector The animated vector
 * @param size Size of the animated vector
 * @param delay Time in millis between animations
 * @param isRunning Animation continues to loop if true
 * @param tint Color of the animated vector
 */
@ExperimentalAnimationGraphicsApi
@Composable
fun LoopingAnimation(
    animatedVector: AnimatedImageVector,
    size: Dp,
    delay: Long = 800,
    isRunning : Boolean,
    tint: Color = MaterialTheme.colors.onSurface
) {

    var atEnd by remember { mutableStateOf(false) }

    suspend fun runAnimation() {
        while (isRunning) {
            kotlinx.coroutines.delay(delay)
            atEnd = !atEnd
        }
    }

    LaunchedEffect(animatedVector) {
        runAnimation()
    }

    Icon(
        painter = rememberAnimatedVectorPainter(
            animatedVector,
            atEnd
        ),
        null,
        Modifier
            .size(size),
        tint = tint,
    )
}