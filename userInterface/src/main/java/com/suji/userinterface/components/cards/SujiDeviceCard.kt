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
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suji.model.*
import com.suji.model.providers.SujiDeviceProvider
import com.suji.sujitechnicalexercise.ui.theme.TextGrey
import com.suji.userinterface.R
import com.suji.userinterface.components.animations.LoopingAnimation
import timber.log.Timber

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SujiDeviceCard(
    @PreviewParameter(SujiDeviceProvider::class)
    sujiDevice: SujiDevice,
    onClick: (String) -> Unit = {},
    athlete: Athlete?

) {

    val color = MaterialTheme.colors
    val typography = MaterialTheme.typography

    val logoColor: Color by animateColorAsState(
        when (sujiDevice.connectionStatus) {
            ConnectionStatus.CONNECTED -> color.primary
            ConnectionStatus.DISCONNECTED -> color.secondary
            ConnectionStatus.CONNECTING -> color.secondary
            ConnectionStatus.DISCONNECTING -> TextGrey
        }
    )

    val text = when (sujiDevice.connectionStatus) {
        ConnectionStatus.CONNECTING -> "Connecting..."
        ConnectionStatus.DISCONNECTED -> sujiDevice.name
        ConnectionStatus.CONNECTED -> sujiDevice.name
        ConnectionStatus.DISCONNECTING -> "Disconnecting..."
    }

    val anim = when (sujiDevice.connectionStatus) {
        ConnectionStatus.CONNECTING -> true
        ConnectionStatus.DISCONNECTED -> false
        ConnectionStatus.CONNECTED -> false
        ConnectionStatus.DISCONNECTING -> false
    }

    val textColor: Color by animateColorAsState(
        when (sujiDevice.connectionStatus) {
            ConnectionStatus.CONNECTED -> color.onSurface
            ConnectionStatus.DISCONNECTED -> color.onSurface
            ConnectionStatus.CONNECTING -> color.onSurface
            ConnectionStatus.DISCONNECTING -> color.onSurface
        }
    )


    

Timber.d("Recomposed!")


    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.surface)
            .padding(
                horizontal = 0.dp,
                vertical = 4.dp
            )

            .noRippleClickable {

            },
        elevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .noRippleClickable {
                    onClick(sujiDevice.name)
                },
            contentAlignment = Alignment.TopCenter,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(sujiDevice.connectionStatus == ConnectionStatus.CONNECTING){
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
                    text = text,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontStyle = typography.h1.fontStyle,
                    fontSize = typography.h6.fontSize,
                )
                if (sujiDevice.connectionStatus == ConnectionStatus.CONNECTED) {
                    Icon(
                        painter = painterResource(id = R.drawable.battery_full),
                        contentDescription = "",
                        modifier = Modifier
                            .rotate(90f)
                            .size(18.dp),
                        tint = Color.Green
                    )
                }
            }

            if (sujiDevice.connectionStatus == ConnectionStatus.CONNECTED) {
                if (athlete != null) {
                    AsyncImage(
                        model =athlete.institutionLogoUrl ,
                        contentDescription = "athlete",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .clip(CircleShape),
                    )
                }
            }

        }

    }
}



