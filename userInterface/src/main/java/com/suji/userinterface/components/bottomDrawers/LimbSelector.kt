package com.suji.userinterface.components.bottomDrawers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suji.domain.model.Limb
import com.suji.domain.model.SujiDevice
import com.suji.userinterface.components.cards.LimbCard

@Composable
fun LimbSelector(
    sujiDevice : SujiDevice,
    selectLimb: (Limb) -> Unit
) {
    val colors = MaterialTheme.colors
    Column(
        Modifier
            .background(colors.background)
            .fillMaxWidth()
    ) {
        Text(
            "Select Limb",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(12.dp)
        )
        LazyRow(
            Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (limb in Limb.values()) {
                item {
                    LimbCard(
                        limb = limb,
                        onClick = { selectLimb(it) })
                }
            }
        }
    }
}