package com.suji.userinterface.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.suji.domain.model.Athlete

/**
 * Provides some sample athletes for Compose Previews
 */
class AthleteProvider : PreviewParameterProvider<Athlete> {
    override val values: Sequence<Athlete>
        get() = sequenceOf(
            Athlete(
                uid = "qwertyuiop",
                name = "Mark Hall",
                institutionLogoUrl = "https://www.st-andrews.ac.uk/assets/university/brand/logos/standard-vertical-black.png",
                lopArm = 113,
                lopLeg = 253,
            ),
            Athlete(
                uid = "asdfghjkl",
                name = "Kate Holmes",
                institutionLogoUrl = "https://www.st-andrews.ac.uk/assets/university/brand/logos/standard-vertical-black.png",
                lopArm = 183,
                lopLeg = 274,
            ),
            Athlete(
                uid = "zxcvbnm",
                name = "David Bell",
                institutionLogoUrl = "https://www.st-andrews.ac.uk/assets/university/brand/logos/standard-vertical-black.png",
                lopArm = 176,
                lopLeg = 234,
            ),
        )
}