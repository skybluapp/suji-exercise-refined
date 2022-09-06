package com.suji.model

/**
 * Represents the connection status of a Suji Device
 */
enum class ConnectionStatus(){
    DISCONNECTED,
    CONNECTED,
    CONNECTING,
    DISCONNECTING
}

/**
 * Represents the inflation status of a Suji Device
 * @property DEFLATED  Suji device is 0% inflated
 * @property INFLATED Suji device inflation is equal to target inflation
 * @property INFLATING Suji device inflation is less than target inflation
 * @property DEFLATING Suji device inflation is greater than target inflation
 */
enum class InflationStatus(){
    DEFLATED,
    INFLATED,
    INFLATING,
    DEFLATING
}

/**
 * Represents limbs on the human body
 */
enum class Limb(name: String){
    ARM("Arm"),
    LEG("Leg")
}
