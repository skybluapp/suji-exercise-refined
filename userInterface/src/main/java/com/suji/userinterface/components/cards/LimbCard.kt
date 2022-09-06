package com.suji.userinterface.components.cards

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suji.model.Limb
import com.suji.userinterface.R
import timber.log.Timber

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
@Preview
fun LimbCard(
    limb: Limb = Limb.LEG,
    onClick: (limb: Limb) -> Unit = {}
) {

    val color = MaterialTheme.colors
    val typography = MaterialTheme.typography

    val icon : Int = when(limb){
        Limb.LEG -> R.drawable.leg
        Limb.ARM -> R.drawable.arm
    }



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
                    onClick(limb)
                },
            contentAlignment = Alignment.TopCenter,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                    Icon(
                        painter = painterResource(
                            icon
                        ),
                        null,
                        Modifier
                            .size(50.dp),
                        tint = color.onSurface
                    )
                    Text(
                        text = limb.name,
                        modifier = Modifier.padding(6.dp),
                        style = typography.h6
                    )
            }
        }
    }
}