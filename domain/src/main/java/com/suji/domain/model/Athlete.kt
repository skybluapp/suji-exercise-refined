package com.suji.domain.model

/**
 * Represents an athlete as an entity
 * @param uid A Unique Identifier for the athlete
 * @param name The athletes full name
 * @param institutionLogoUrl A URL for an image representing the athletes institution
 * @param lopArm The Limb Occlusion Pressure for the athletes arm
 * @param lopLeg The Limb Occlusion Pressure for the athletes leg
 */
data class Athlete(
    val uid: String,
    val name: String,
    val institutionLogoUrl: String,
    val lopArm: Int,
    val lopLeg: Int,
)


