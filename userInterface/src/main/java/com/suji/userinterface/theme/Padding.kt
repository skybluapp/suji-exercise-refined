package com.suji.userinterface.theme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Class containing dimensions used for padding values in the UI
 */
data class Dimensions(
    val none : Dp = 0.dp,
    val xSmall : Dp = 4.dp,
    val small : Dp = 6.dp,
    val medium : Dp = 12.dp,
    val large : Dp = 16.dp,
    val xLarge : Dp = 36.dp,
    val elevation : Dp = 0.dp,
    val selectorCardHeight : Dp = 150.dp,
)

val dimensions = Dimensions()