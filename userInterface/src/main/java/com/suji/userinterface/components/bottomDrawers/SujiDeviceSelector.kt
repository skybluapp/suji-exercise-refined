package com.suji.userinterface.components.bottomDrawers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suji.domain.model.SujiDevice
import com.suji.userinterface.components.cards.SujiDeviceCard
import com.suji.userinterface.components.cards.noRippleClickable
import com.suji.userinterface.theme.dimensions

@Composable
fun SujiDeviceSelector(
    sujiDeviceList: List<SujiDevice>,
    selectSujiDevice: (SujiDevice) -> Unit,
    scanForDevices: () -> Unit
) {
    val colors = MaterialTheme.colors
    Column(
        Modifier
            .background(colors.background)
            .fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Select Suji Device",
                color = colors.onBackground,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(12.dp)
            )
            Text(
                "Scan for devices",
                color = colors.primary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(12.dp)
                    .noRippleClickable {
                        scanForDevices()
                    },
            )
        }

        LazyRow(
            Modifier
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {

            item {
                Spacer(modifier = Modifier.width(dimensions.small))
            }

            for (sujiDevice in sujiDeviceList) {

                item {
                    SujiDeviceCard(
                        sujiDevice = sujiDevice,
                        onClick = { selectSujiDevice(sujiDevice) },
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.width(dimensions.small))
            }
        }
    }
}