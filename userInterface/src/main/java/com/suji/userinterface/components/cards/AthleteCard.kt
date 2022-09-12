package com.suji.userinterface.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suji.domain.model.Athlete
import com.suji.domain.model.ConnectionStatus
import com.suji.domain.model.Limb
import com.suji.domain.model.SujiDevice
import com.suji.sujitechnicalexercise.ui.theme.TextGrey
import com.suji.userinterface.R
import com.suji.userinterface.components.animations.animatedShimmer
import com.suji.userinterface.providers.AthleteProvider
import com.suji.userinterface.theme.dimensions
import timber.log.Timber

/**
 * @param athlete The athlete for which data is displayed
 * @param onAthleteClicked function that is called when profile picture is clicked
 * @param onSelectDeviceClicked function that is called when 'Select Device' button is clicked
 * @param sujiDevice the Suji device assigned to this athlete
 * @param inflateToPercentage function to call when user requests device is inflated to specified percentage
 * @param stopAndDeflate function to call when user requests device is stopped and deflated
 **/
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Preview
@Composable
fun AthleteCard(
    @PreviewParameter(AthleteProvider::class) athlete: Athlete,
    onAthleteClicked: () -> Unit = {},
    onSelectDeviceClicked: (Athlete) -> Unit = {},
    sujiDevice: SujiDevice? = null,
    onClick: (SujiDevice) -> Unit = {},
    backgroundColor : Color = MaterialTheme.colors.surface
) {

    val color = MaterialTheme.colors
    val shape = MaterialTheme.shapes

    Card(
        modifier = Modifier
            .clip(shape.medium)
            .background(color = backgroundColor)
            .fillMaxWidth()
            .noRippleClickable {
                if (sujiDevice?.connectionStatus == ConnectionStatus.CONNECTED) {
                    onClick(sujiDevice)
                }
            },
        elevation = dimensions.elevation,

        ) {
        Column(Modifier.fillMaxWidth()) {
            AthleteStatus(
                athlete = athlete,
                onAthleteClicked = {onAthleteClicked()},
                onSelectDeviceClicked = { onSelectDeviceClicked(athlete) },
                sujiDevice = sujiDevice
            )

            if(sujiDevice != null && sujiDevice.lopArm != 0 && sujiDevice.lopLeg != 0){
                Row(
                    Modifier
                        .clip(shape = shape.medium)
                        .background(color.secondary)
                        .fillMaxWidth()
                        .height(6.dp)
                ) {


                    val assignedLimb = if(sujiDevice.assignedLimb == Limb.LEG) sujiDevice.lopLeg else sujiDevice.lopArm
                    val p = sujiDevice.pressure.toFloat().div(assignedLimb)
                    Timber.d("${sujiDevice.pressure} / $assignedLimb Percentage = $p")
                    Row(
                        Modifier
                            .fillMaxWidth(p)
                            .clip(shape.medium)
                            .background(colors.primary)

                            .height(6.dp)
                    ){

                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AthleteStatus(
    athlete: Athlete,
    onAthleteClicked: () -> Unit,
    onSelectDeviceClicked: (Athlete) -> Unit,
    sujiDevice: SujiDevice?
) {
    val colors = MaterialTheme.colors
    val shape = MaterialTheme.shapes

    Row() {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .height(139.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AsyncImage(
                model = athlete.institutionLogoUrl,
                contentDescription = "athlete",
                contentScale = ContentScale.Crop,
                modifier = Modifier

                    .size(75.dp)
                    .padding(dimensions.medium)

                    .clip(CircleShape)
                    .background(animatedShimmer())
                    .noRippleClickable {
                        onAthleteClicked()
                    }
            )
            Text(
                text = athlete.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = typography.h6,
                color = colors.onSurface
            )

        }
        Column(
            modifier = Modifier
                .weight(0.1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.data_exchange),
                    contentDescription = "Exchange",
                    tint = TextGrey
                )
            }

        }
        Column(
            modifier = Modifier
                .weight(0.45f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SujiDeviceComp(
                sujiDevice = sujiDevice,
                onTextClicked = {
                    if (sujiDevice == null) {
                        onSelectDeviceClicked(athlete)
                    }
                })


        }
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

