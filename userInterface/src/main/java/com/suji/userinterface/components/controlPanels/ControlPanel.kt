package com.suji.userinterface.components.controlPanels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.suji.domain.model.Athlete
import com.suji.domain.model.InflationStatus
import com.suji.domain.model.SujiDevice
import com.suji.userinterface.components.cards.AthleteCard
import com.suji.userinterface.theme.dimensions
import timber.log.Timber
import java.math.BigDecimal

enum class InflationPercentages(val number: Int) {
    FIFTY_PERCENT(50),
    SIXTY_PERCENT(60),
    SEVENTY_PERCENT(70),
    EIGHTY_PERCENT(80)
}

/**
 * Control panel to control a Suji device
 */
@Composable
fun SujiControlPanel(
    onStopAndDeflate: () -> Unit,
    onSelectLimb: () -> Unit,
    onInflateToPercentage: (Int) -> Unit,
    sujiDevice: SujiDevice,
    athlete: Athlete,
    onDisconnect: () -> Unit
) {

    var selectedPercentage by remember {
        mutableStateOf(50)
    }

    val color = MaterialTheme.colors
    val shape = MaterialTheme.shapes
    val typography = MaterialTheme.typography



    Column(
        Modifier
            .fillMaxWidth()
            .background(color = color.background)
    ) {
        Column() {

            Column(Modifier.padding(dimensions.medium)) {
                AthleteCard(
                    athlete = athlete,
                    sujiDevice = sujiDevice,
                    backgroundColor = MaterialTheme.colors.background
                )



            }

        }
        Row {
            for (percentage in InflationPercentages.values()) {
                Row(
                    Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPercentage == percentage.number,
                        onClick = { selectedPercentage = percentage.number },
                        colors = RadioButtonDefaults.colors(selectedColor = color.primary),
                        enabled = sujiDevice.assignedLimb != null
                    )
                    Text(
                        text = "${percentage.number}%",
                        color = color.onSurface
                    )
                }
            }

        }
        Column(
            Modifier.padding(
                start = dimensions.medium,
                end = dimensions.medium,
                bottom = dimensions.medium
            ),
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {

            Button(
                enabled = sujiDevice.assignedLimb != null && sujiDevice.inflationStatus != InflationStatus.INFLATING && sujiDevice.inflationStatus != InflationStatus.DEFLATING,
                onClick = { onInflateToPercentage(0) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
            )
            {
                Text(
                    "Deflate",
                    Modifier.padding(dimensions.small)
                )
            }
            Button(
                enabled = sujiDevice.assignedLimb != null && sujiDevice.inflationStatus != InflationStatus.INFLATING && sujiDevice.inflationStatus != InflationStatus.DEFLATING,
                onClick = { onInflateToPercentage(selectedPercentage) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
            )
            {
                Text(
                    "Inflate Cuff to ${selectedPercentage}%",
                    Modifier.padding(dimensions.small)
                )
            }
            Button(
                onClick = { onSelectLimb() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
            )
            {
                Text(
                    "Calibrate To Limb",
                    Modifier.padding(dimensions.small)
                )
            }

            Button(
                onClick = { onDisconnect() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = color.primary)
            )
            {
                Text(
                    "Disconnect",
                    Modifier.padding(dimensions.small)
                )
            }

        }

    }
}