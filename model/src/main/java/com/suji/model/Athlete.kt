package com.suji.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class Athlete(
    val uid: String,
    val name: String,
    val institutionLogoUrl: String,
    val lopArm: Int,
    val lopLeg: Int,
)


