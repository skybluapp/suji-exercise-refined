package com.suji.userinterface.components.bottomDrawers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suji.domain.model.Limb
import com.suji.userinterface.theme.dimensions

@Preview
@Composable
fun FilterSelector(
    filtered: Boolean = true,
    filteredByLimb: Limb? = null,
    onToggleFilterConnected: (Boolean) -> Unit = {},
    onFilterLimb: (Limb?) -> Unit = {}
) {
    val color = MaterialTheme.colors
    val typography = MaterialTheme.typography
    Column(
        Modifier
            .background(color.background)
            .fillMaxWidth()
    ) {
        Text(
            "Filter Athletes",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.medium,
                    end = dimensions.medium
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Show Only Connected Athletes",
                style = typography.body2
            )
            Switch(
                filtered,
                onCheckedChange = { onToggleFilterConnected(!filtered) })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensions.medium,
                    end = dimensions.medium
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Filter By Calibrated Limb",
                style = typography.body2
            )
            Switch(
                filteredByLimb != null,
                onCheckedChange = {
                    if (filteredByLimb == null) {
                        onFilterLimb(Limb.ARM)
                    } else {
                        onFilterLimb(null)
                    }
                }

            )
        }
        AnimatedVisibility(visible = filteredByLimb != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensions.medium,
                        end = dimensions.medium
                    ),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (limb in Limb.values()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(dimensions.medium)
                    ) {
                        RadioButton(
                            selected = filteredByLimb == limb,
                            onClick = { onFilterLimb(limb) },
                            colors = RadioButtonDefaults.colors(selectedColor = color.primary),
                        )
                        Text(
                            text = limb.text,
                            color = color.onSurface
                        )
                    }

                }
            }
        }

    }
}