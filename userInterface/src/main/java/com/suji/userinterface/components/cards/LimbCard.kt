package com.suji.userinterface.components.cards

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suji.domain.model.Limb
import com.suji.userinterface.R
import com.suji.userinterface.theme.dimensions
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

    val cardContent = @Composable {
        val icon : Int = when(limb){
            Limb.LEG -> R.drawable.leg
            Limb.ARM -> R.drawable.arm
        }
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
            text = limb.text,
            modifier = Modifier.padding(dimensions.small),
            style = typography.h6
        )
    }

    SelectorCard( content = {cardContent()}, onClick = {onClick(limb)})
}