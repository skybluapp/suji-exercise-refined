package com.connectedSujiDevices

import com.suji.model.Athlete
import com.suji.model.ConnectionStatus
import com.suji.model.SujiDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

interface ConnectedSujiDevicesInterface {

    suspend fun connectSujiToAthlete(
        athlete: Athlete,
        sujiDevice: String
    )

    suspend fun disconnectSujiFromAthlete(deviceName: String)
    fun getAllSujiDevices(): Array<SujiDevice>
    fun getSujiDeviceByName(name: String): SujiDevice?
    fun getSujiDeviceFromAthlete(name: String): SujiDevice?
    val sujiDeviceFlow: StateFlow<Array<SujiDevice>>
    val sujiAthleteMapFlow: StateFlow<Map<Athlete, SujiDevice?>>
    suspend fun addSujiDevice(name: String)
    fun addAthlete(athlete: Athlete)
    fun removeAthlete(athlete: Athlete)
    suspend fun scanForDevices()
    fun wipe()

}

class ConnectedSujiDevices : ConnectedSujiDevicesInterface {

    //Contains an array of Suji devices. Setting value property will cause sujiDeviceFlow
    //to emit a new value
    private val _sujiDeviceFlow: MutableStateFlow<Array<SujiDevice>> = MutableStateFlow(
        arrayOf(
            SujiDevice("Suji-E2802"),
            SujiDevice("Suji-A609"),
            SujiDevice("Suji-K903")
        )
    )

    //Emits a new array containing all Suji devices when the value in _sujiDeviceFlow is updated
    override val sujiDeviceFlow: StateFlow<Array<SujiDevice>> = _sujiDeviceFlow

    //Contains a mapping of Athlete to Suji Devices
    private val _sujiAthleteMapFlow: MutableStateFlow<Map<Athlete, SujiDevice?>> =
        MutableStateFlow(mapOf())
    override val sujiAthleteMapFlow: StateFlow<Map<Athlete, SujiDevice?>> = _sujiAthleteMapFlow

    override suspend fun connectSujiToAthlete(
        athlete: Athlete,
        sujiDevice: String
    ) {
        sujiDeviceFlow.value.find { arrayDevice -> arrayDevice.name == sujiDevice }.also { suji ->
            try {
                if (suji != null) {
                    if (suji.connectionStatus == ConnectionStatus.DISCONNECTED) {
                        pair(
                            athlete,
                            suji
                        )
                        suji.connectionStatus = ConnectionStatus.CONNECTING
                        updateSuji(suji)
                        pair(
                            athlete,
                            suji
                        )
                        delay(5000)
                        suji.connectionStatus = ConnectionStatus.CONNECTED
                        suji.lopLeg = athlete.lopLeg
                        suji.lopArm = athlete.lopArm
                        suji.assignedLimb = null
                        updateSuji(suji)
                        pair(
                            athlete,
                            suji
                        )

                    }
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                Timber.d("A Suji device with this name is not connected!")
            }
        }
    }

    private fun updateSuji(sujiDevice: SujiDevice) {
        val index = sujiDeviceFlow.value.indexOf(sujiDevice)
        val array = sujiDeviceFlow.value.clone()
        array[index] = sujiDevice
        _sujiDeviceFlow.value = array
    }

    override fun addAthlete(athlete: Athlete) {
        if (sujiAthleteMapFlow.value[athlete] == null) {
            val copy = sujiAthleteMapFlow.value
            val new = copy + Pair(
                athlete,
                null
            )
            _sujiAthleteMapFlow.value = new
        }

    }

    private fun pair(
        athlete: Athlete,
        sujiDevice: SujiDevice
    ) {
        val copy: MutableMap<Athlete, SujiDevice?> = mutableMapOf()
        sujiAthleteMapFlow.value.forEach {
            copy[it.key] = it.value
        }
        copy[athlete] = sujiDevice.copy()
        _sujiAthleteMapFlow.value = copy
    }

    override fun removeAthlete(athlete: Athlete) {
        val copy: MutableMap<Athlete, SujiDevice?> = mutableMapOf()
        sujiAthleteMapFlow.value.forEach {
            copy[it.key] = it.value
        }
        copy.remove(athlete)
        _sujiAthleteMapFlow.value = copy
    }

    override suspend fun disconnectSujiFromAthlete(athleteName: String) {

    }

    override fun getAllSujiDevices(): Array<SujiDevice> {
        return sujiDeviceFlow.value
    }

    override fun getSujiDeviceByName(name: String): SujiDevice? {
        return sujiDeviceFlow.value.find { device -> device.name == name }
    }

    override fun getSujiDeviceFromAthlete(name: String): SujiDevice? {
        TODO("Not yet implemented")
    }

    //Adds a Suji device to the flow (simulates a device being connected over Bluetooth)
    override suspend fun addSujiDevice(name: String) {
        _sujiDeviceFlow.value = sujiDeviceFlow.value + SujiDevice(name = name)
    }

    //Adds a Suji device to the flow (simulates a device being connected over Bluetooth)
    override suspend fun scanForDevices() {
        addSujiDevice("Suji-W893")
        addSujiDevice("Suji-G893")
        addSujiDevice("Suji-O930")
    }

    override fun wipe(){
        _sujiAthleteMapFlow.value = mapOf()
    }

}