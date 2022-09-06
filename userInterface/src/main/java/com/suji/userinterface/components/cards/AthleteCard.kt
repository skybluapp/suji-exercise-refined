package com.suji.userinterface.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suji.model.*
import com.suji.model.providers.AthleteProvider
import com.suji.sujitechnicalexercise.ui.theme.TextGrey
import com.suji.userinterface.R
import com.suji.userinterface.components.animations.LoopingAnimation
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
@Preview
@Composable
fun AthleteCard(
    @PreviewParameter(AthleteProvider::class) athlete: Athlete,
    onAthleteClicked: () -> Unit = {},
    onSelectDeviceClicked: (Athlete) -> Unit = {},
    sujiDevice: SujiDevice? = null,
    inflateToPercentage: (Int) -> Unit = {},
    stopAndDeflate: () -> Unit = {},
    openSelectLimb: () -> Unit = {}
) {

    val color = MaterialTheme.colors
    val shape = MaterialTheme.shapes

    var enabled by remember {
        mutableStateOf(false)
    }
    var selectedPercentage by remember {
        mutableStateOf(50)
    }

    val rotation: Float by animateFloatAsState(if (enabled) 180f else 0f)


    Card(
        modifier = Modifier
            .clip(shape.medium)
            .background(color.surface)
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )

            .fillMaxWidth()
            .noRippleClickable {
                enabled = !enabled
            },
        elevation = 0.dp,

        ) {
        Column(Modifier.fillMaxWidth()) {
            AthleteStatus(
                athlete = athlete,
                onAthleteClicked = { /*TODO*/ },
                onSelectDeviceClicked = {onSelectDeviceClicked(athlete) },
                sujiDevice = sujiDevice
            )
            if (sujiDevice?.connectionStatus  == ConnectionStatus.CONNECTED) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down),
                            contentDescription = "expand",
                            tint = color.primary,
                            modifier = Modifier.rotate(rotation),

                            )
                    }

                }

                AnimatedVisibility(visible = enabled) {
                    Column {

                        Row {
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPercentage == 50,
                                    onClick = { selectedPercentage = 50 },
                                    colors = RadioButtonDefaults.colors(selectedColor = color.primary)
                                )
                                Text(text = "50%")
                            }
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPercentage == 60,
                                    onClick = { selectedPercentage = 60 },
                                    colors = RadioButtonDefaults.colors(selectedColor = color.primary)
                                )
                                Text(text = "60")
                            }
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPercentage == 70,
                                    onClick = { selectedPercentage = 70 },
                                    colors = RadioButtonDefaults.colors(selectedColor = color.primary)
                                )
                                Text(text = "70%")
                            }
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPercentage == 80,
                                    onClick = { selectedPercentage = 80 },
                                    colors = RadioButtonDefaults.colors(selectedColor = color.primary)
                                )
                                Text(text = "80%")
                            }
                        }
                        Column(
                            Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            Button(
                                enabled = sujiDevice.assignedLimb != null,
                                onClick = { stopAndDeflate() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
                            )
                            {
                                Text("Stop & Deflate")
                            }


                            Button(
                                enabled = sujiDevice.assignedLimb != null,
                                onClick = { inflateToPercentage(selectedPercentage) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
                            )
                            {
                                Text("Inflate Cuff to ${selectedPercentage}%")
                            }

                            Button(
                                onClick = {openSelectLimb()},
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
                            )
                            {
                                Text("Select Limb")
                            }

                        }

                    }

                }
            }
        }
    }
}

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



@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AthleteStatus(
    athlete: Athlete,
    onAthleteClicked: () -> Unit,
    onSelectDeviceClicked: (Athlete) -> Unit,
    sujiDevice: SujiDevice?
) {
    val colors = MaterialTheme.colors

    val logoColor: Color by animateColorAsState(
        when (sujiDevice?.connectionStatus) {
            ConnectionStatus.CONNECTED -> colors.primary
            ConnectionStatus.DISCONNECTED -> TextGrey
            ConnectionStatus.CONNECTING -> colors.secondary
            ConnectionStatus.DISCONNECTING -> TextGrey

            else -> {
                TextGrey
            }
        }
    )

    Row(Modifier.padding(bottom = 12.dp)) {
        Column(
            modifier = Modifier
                .weight(0.45f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier.size(150.dp)) {

                AsyncImage(
                    model = athlete.institutionLogoUrl,
                    contentDescription = "athlete",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(75.dp)
                        .padding(4.dp)

                        .clip(CircleShape)
                        .background(animatedShimmer())
                        .noRippleClickable {
                            onAthleteClicked()
                        }
                )
                Text(
                    text = athlete.name,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = typography.h6
                )
            }

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
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {


                if(sujiDevice?.connectionStatus == ConnectionStatus.CONNECTING){
                    LoopingAnimation(
                        animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.red_suji_animated),
                        size = 100.dp,
                        isRunning = true,
                        tint = logoColor,
                    )
                } else {
                    LoopingAnimation(
                        animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.red_suji_animated),
                        size = 100.dp,
                        isRunning = false,
                        tint = logoColor,
                    )
                }



                Text(
                    text = when (sujiDevice?.connectionStatus) {
                        ConnectionStatus.CONNECTING -> "Connecting..."
                        ConnectionStatus.DISCONNECTED -> "Select Device"
                        ConnectionStatus.CONNECTED -> sujiDevice.name + " " + sujiDevice.assignedLimb?.name
                        ConnectionStatus.DISCONNECTING -> "Disconnecting..."

                        else -> {
                            "Select Device"
                        }
                    },
                    color = when (sujiDevice?.connectionStatus) {
                        ConnectionStatus.CONNECTING -> colors.onSurface
                        ConnectionStatus.DISCONNECTED -> colors.primary
                        ConnectionStatus.CONNECTED -> colors.onSurface
                        ConnectionStatus.DISCONNECTING -> TextGrey

                        else -> {
                            colors.primary
                        }
                    },

                    fontStyle = typography.h6.fontStyle,
                    fontSize = typography.h6.fontSize,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .noRippleClickable {
                            if (sujiDevice == null) {
                                onSelectDeviceClicked(athlete)
                            }
                        }
                )

                if (sujiDevice?.connectionStatus == ConnectionStatus.CONNECTED) {
                    Icon(
                        painter = painterResource(id = R.drawable.battery_full),
                        contentDescription = "",
                        modifier = Modifier
                            .offset(y = 19.dp)
                            .align(Alignment.BottomCenter)
                            .rotate(90f)
                            .size(18.dp),
                        tint = Color.Green
                    )
                }
            }

        }
    }
}


inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

