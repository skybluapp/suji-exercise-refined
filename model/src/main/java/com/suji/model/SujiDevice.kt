package com.suji.model


import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.random.Random

/**
 * Represents a Suji Device as a data object
 * @param name The name of the device, should be in format "Suji-123"
 * @param connectionStatus Represents if the device is connected to an athlete
 * @param inflationStatus Represents the inflation state of the device
 * @param pressure The pressure of the device as a percentage of the athletes LOC
 * @param batteryPercentage The devices battery percentage
 * @param lopArm The limb Occlusion Pressure of the connected athletes arm
 * @param lopArm The limb Occlusion Pressure of the connected athletes leg
 * @param assignedLimb The limb the athlete has set the device to. Device will inflate to the LOC of this limb
 */
data class SujiDevice(
    var name : String = sujiNameCreator(),
    var connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    var inflationStatus: InflationStatus = InflationStatus.DEFLATED,
    var pressure : Int = 0,
    var batteryPercentage : Int = 100,
    var lopLeg : Int = 0,
    var lopArm : Int = 0,
    var assignedLimb : Limb? = null
)

/**
 * Creates a random name for a Suji Device with the format "Suji-A123"
 */
fun sujiNameCreator() : String {
    return "Suji-" +
            ('A'..'Z').random() +
            Random.nextInt(0,10) +
            Random.nextInt(0,10) +
            Random.nextInt(0,10)
}




