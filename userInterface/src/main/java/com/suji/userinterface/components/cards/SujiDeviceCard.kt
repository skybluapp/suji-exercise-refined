package com.suji.userinterface.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

import com.suji.domain.model.ConnectionStatus
import com.suji.domain.model.InflationStatus
import com.suji.domain.model.SujiDevice
import com.suji.userinterface.providers.SujiDeviceProvider
import com.suji.sujitechnicalexercise.ui.theme.TextGrey
import com.suji.userinterface.R
import com.suji.userinterface.components.animations.LoopingAnimation

/**
 * Displays a card containing the status of a Suji Device
 * @param sujiDevice The Suji device to display
 * @param onClick An action to do when the card is clicked
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SujiDeviceCard(
    @PreviewParameter(SujiDeviceProvider::class)
    sujiDevice: SujiDevice,
    onClick: (String) -> Unit = {},
) {
    SelectorCard(
        onClick = {onClick(sujiDevice.name)}
    ) {
        SujiDeviceComp(sujiDevice)
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SujiDeviceComp(
    sujiDevice: SujiDevice?,
    onTextClicked : () -> Unit = {}
){

    val color = MaterialTheme.colors

    //Select color of Suji logo based upon connection status
    val logoColor: Color by animateColorAsState(
        when (sujiDevice?.connectionStatus) {
            ConnectionStatus.CONNECTED -> color.primary
            ConnectionStatus.DISCONNECTED -> color.secondary
            ConnectionStatus.CONNECTING -> color.secondary
            ConnectionStatus.DISCONNECTING -> TextGrey
            else -> TextGrey
        }
    )

    //Select color of Suji logo based upon connection status
    val textColor: Color by animateColorAsState(
        when (sujiDevice?.connectionStatus) {
            ConnectionStatus.CONNECTED -> color.onSurface
            ConnectionStatus.DISCONNECTED -> color.onSurface
            ConnectionStatus.CONNECTING -> color.onSurface
            ConnectionStatus.DISCONNECTING -> color.onSurface
            else -> color.primary
        }
    )

    //Select text based upon connection status
    val text = when (sujiDevice?.connectionStatus) {
        ConnectionStatus.CONNECTING -> "Connecting..."
        ConnectionStatus.DISCONNECTED -> sujiDevice.name
        ConnectionStatus.CONNECTED -> sujiDevice.name
        ConnectionStatus.DISCONNECTING -> "Disconnecting..."
        else -> {"Select Device"}
    }

    //Select battery indicator colour based on battery percentage
    val batteryColor: Color by animateColorAsState(
        when(sujiDevice?.batteryPercentage){
            in 0..20 -> Color.Red
            in 20..50 -> Color(0xFFFFA500)
            in 50..100 -> Color.Green
            else -> Color.Transparent
        }
    )

    //If the device is connecting, animate the logo

        if(sujiDevice?.connectionStatus == ConnectionStatus.CONNECTING){
            LoopingAnimation(
                animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.red_suji_animated),
                size = 100.dp,
                isRunning = true,
                tint = logoColor,
            )
        } else {
            when(sujiDevice?.inflationStatus){
                InflationStatus.INFLATING -> {
                    LoopingAnimation(
                        animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.suji_inflating_animated),
                        size = 100.dp,
                        isRunning = true,
                        tint = logoColor,
                    )
                }
                InflationStatus.DEFLATING -> {
                    LoopingAnimation(
                        animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.suji_deflating_animated),
                        size = 100.dp,
                        isRunning = true,
                        tint = logoColor,
                    )
                }
                else -> {
                    LoopingAnimation(
                        animatedVector = AnimatedImageVector.animatedVectorResource(R.drawable.red_suji_animated),
                        size = 100.dp,
                        isRunning = false,
                        tint = logoColor,
                    )
                }
            }

        }


    //Show Text
    Text(
        style = typography.h6,
        color = textColor,
        text = text,
        modifier = Modifier.noRippleClickable {
            onTextClicked()
        }
    )

    //Battery Icon
    Icon(
        painter = painterResource(id = R.drawable.battery_full),
        contentDescription = "Battery Indicator",
        modifier = Modifier
            .rotate(90f)
            .size(18.dp),
        tint = batteryColor
    )
}



