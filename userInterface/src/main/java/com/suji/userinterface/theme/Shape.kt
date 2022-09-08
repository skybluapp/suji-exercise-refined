package com.suji.sujitechnicalexercise.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp
import com.suji.userinterface.theme.dimensions

val Shapes = Shapes(
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(0.dp)
)

val bottomSheetShape = RoundedCornerShape(
    dimensions.small,
    dimensions.small,
    0.dp,
    0.dp
)
