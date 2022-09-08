package com.suji.userinterface.components.cards

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.suji.userinterface.components.animations.animatedShimmer

/**
 * @param athlete The athlete for which data is displayed
 * @param onAthleteClicked function that is called when profile picture is clicked
 * @param onSelectDeviceClicked function that is called when 'Select Device' button is clicked
 * @param sujiDevice the Suji device assigned to this athlete
 * @param inflateToPercentage function to call when user requests device is inflated to specified percentage
 * @param stopAndDeflate function to call when user requests device is stopped and deflated
 **/
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AthleteCardPlaceholder() {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(animatedShimmer())
            .padding(
                horizontal = 10.dp,
                vertical = (85).dp
            )
            .fillMaxWidth(),
        elevation = 0.dp,

        ) {

    }
}