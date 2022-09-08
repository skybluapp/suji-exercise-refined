package com.suji.domain.model

/**
 * Represents the connection status of a Suji Device
 */
enum class ConnectionStatus(){
    CONNECTED,
    CONNECTING,
    DISCONNECTING,
    DISCONNECTED,
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
enum class Limb(val text: String){
    ARM("Arm"),
    LEG("Leg")
}

/**
 * Represents different bottom drawers that can be displayed
 */
enum class DashboardBottomDrawers {
    SELECT_SUJI,
    SELECT_LIMB,
    SUJI_CONTROL_PANEL,
    REASSIGN_ATHLETES,
    FILTER,
    NONE
}
