package com.suji.domain.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.suji.domain.model.Athlete

/**
 * Maps a Firebase document to an athlete
 */
fun DocumentSnapshot.toAthlete(): Athlete {
    return Athlete(
        uid = this["uid"].toString(),
        name = this["name"].toString(),
        institutionLogoUrl = this["institutionLogoURL"].toString(),
        lopLeg = this["lopLeg"].toString().toInt(),
        lopArm = this["lopArm"].toString().toInt()
    )
}