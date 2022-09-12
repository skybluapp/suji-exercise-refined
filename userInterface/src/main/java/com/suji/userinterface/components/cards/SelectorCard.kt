package com.suji.userinterface.components.cards

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.suji.domain.model.Limb
import com.suji.userinterface.R
import com.suji.userinterface.theme.dimensions
import timber.log.Timber

/**
 * Card used to display custom content for the user to select from
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SelectorCard(
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val color = MaterialTheme.colors
    val shape = MaterialTheme.shapes

    Card(
        modifier = Modifier


            .padding(
                horizontal = dimensions.none,
                vertical = dimensions.xSmall
            )
            .clip(shape.medium)
            .background(color.surface)
            .noRippleClickable {

            },
        elevation = dimensions.elevation,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.selectorCardHeight)
                .noRippleClickable {
                    onClick()
                },
            contentAlignment = Alignment.TopCenter,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}