package com.suji.userinterface.components.selectors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suji.model.Athlete
import com.suji.model.SujiDevice
import com.suji.userinterface.components.cards.SujiDeviceCard
import com.suji.userinterface.components.cards.noRippleClickable

@Composable
fun SujiDeviceSelector(
    sujiDeviceList: SnapshotStateList<SujiDevice>,
    selectSujiDevice: (String) -> Unit,
    map: Map<Athlete, SujiDevice?>,
    scanForDevices : () -> Unit
) {
    val colors = MaterialTheme.colors
    Column(Modifier.background(colors.background)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(
                "Select Suji Device",
                color = colors.onBackground,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(12.dp)
            )
            Text(
                "Scan for devices",
                color = colors.primary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(12.dp).noRippleClickable {
                    scanForDevices()
                },
            )
        }

        LazyRow(
            Modifier
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            for (sujiDevice in sujiDeviceList) {
                val p = map.filter { it -> it.value == sujiDevice }
                item {
                    SujiDeviceCard(
                        sujiDevice = sujiDevice,
                        onClick = { selectSujiDevice(it) },
                        athlete = p.keys.firstOrNull()
                    )
                }
            }
        }
    }
}