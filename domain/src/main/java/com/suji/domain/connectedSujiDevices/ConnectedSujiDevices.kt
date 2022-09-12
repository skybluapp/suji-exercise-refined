package com.suji.domain.connectedSujiDevices

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.suji.domain.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 * @property unassignedAthletes Contains a list of athletes not paired with a device
 * @property unassignedSujiDevices Contains a list of suji devices not paired with athletes
 * @property athleteDeviceMap Contains a mapping of paired athletes to paired devices
 * @property addSujiDevice Adds an unassigned Suji device
 * @property addAthlete Adds an unpaired athlete
 * @property getNumberOfAthletes returns the total number of athletes paired and unpaired
 * @property getNumberOfSujis returns the total number of suji devices paired and unpaired
 * @property replaceSujiDevice A function to change the values inside a suji device. Device Name should never be changed
 * @property connectSujiToAthlete Pairs a suji device with an athlete
 * @property clearAthletes Removes all athletes
 * @property reassignEvent Reassigns a Suji device to a new athlete
 * @property inflateToPercentage Inflates a Suji device to a specified percentage of an athletes LOC
 * @property directionality Disconnects a suji device from an athlete
 */
interface SujiDeviceManager {
    val unassignedAthletes: StateFlow<List<Athlete>>
    val unassignedSujiDevices: StateFlow<List<SujiDevice>>
    val athleteDeviceMap : StateFlow<BiMap<Athlete, SujiDevice>>
    fun addAthlete(athlete: Athlete)
    fun addSujiDevice(sujiDevice: SujiDevice)
    fun getNumberOfAthletes() : Int
    fun getNumberOfSujis() : Int
    fun replaceSujiDevice(oldSujiDeviceName: String, newSujiDevice: SujiDevice)
    suspend fun connectSujiToAthlete(athleteUID: String, deviceName: String): Boolean
    fun clearAthletes()
    val reassignEvent : SharedFlow<Pair<Athlete, Athlete>>
    val updateSujiEvent : SharedFlow<SujiDevice>
    suspend fun inflateToPercentage(deviceName : String, percentage : Int)
    suspend fun disconnectSujiFromAthlete(deviceName : String, athleteUID: String) : Boolean
    fun caliabrateToLimb(deviceName: String, limb: Limb)
}

/**
 * One-to-one relationship between Suji device and Athlete is modelled as two maps.
 * @property deviceNameToAthlete Provides access to an Athlete object from a provided Suji device name
 * @property athleteUIDtoSujiDevice Provides access to a Suji device from a provided athlete UID
 */
class ConnectedSujiDevicesBimap : SujiDeviceManager {

    private val _athleteDeviceMap: MutableStateFlow<BiMap<Athlete, SujiDevice>> =  MutableStateFlow(HashBiMap.create())
    override val athleteDeviceMap: StateFlow<BiMap<Athlete, SujiDevice>> = _athleteDeviceMap

    private val _unassignedAthletes : MutableStateFlow<List<Athlete>> = MutableStateFlow(listOf())
    override val unassignedAthletes: StateFlow<List<Athlete>> = _unassignedAthletes

    private val _unassignedSujiDevices : MutableStateFlow<List<SujiDevice>> = MutableStateFlow(startingArray.toList())
    override val unassignedSujiDevices: StateFlow<List<SujiDevice>> = _unassignedSujiDevices

    private val _reassignEvent = MutableSharedFlow<Pair<Athlete, Athlete>>()
    override val reassignEvent = _reassignEvent.asSharedFlow()

    private val _updateSujiEvent = MutableSharedFlow<SujiDevice>()
    override val updateSujiEvent = _updateSujiEvent.asSharedFlow()



    private suspend fun reassignEvent(new : Athlete, current : Athlete) {
        _reassignEvent.emit(Pair(new, current))
    }

    override fun addAthlete(athlete: Athlete) {
        if(getAthlete(athlete.uid) == null){
            val copy = unassignedAthletes.value.toMutableList()
            copy.add(athlete)
            _unassignedAthletes.value = copy
        }
    }

    override fun addSujiDevice(sujiDevice: SujiDevice) {
        val copy = unassignedSujiDevices.value.toMutableList()
        copy.add(sujiDevice)
        _unassignedSujiDevices.value = copy
    }

    override fun getNumberOfAthletes(): Int {
        return unassignedAthletes.value.size + athleteDeviceMap.value.size
    }

    override fun getNumberOfSujis(): Int {
        return unassignedSujiDevices.value.size + athleteDeviceMap.value.size
    }


    fun getSujiDevice(deviceName: String): SujiDevice? {
        return getAllSujiDevices().find { device -> device.name == deviceName}
    }

    fun getAthlete(athleteUID: String): Athlete? {
        return getAllAthletes().find { athlete -> athlete.uid == athleteUID}
    }

    private fun isSujiDeviceConnected(deviceName: String) : Boolean{
        return athleteDeviceMap.value.values.find { device -> device.name == deviceName } != null
    }

    private fun isAthleteConnected(athleteUID: String) : Boolean{
        return athleteDeviceMap.value.keys.find { athlete -> athlete.name == athleteUID } != null
    }

    private fun getAllAthletes() : List<Athlete>{
        return unassignedAthletes.value + athleteDeviceMap.value.keys
    }

    private fun getAllSujiDevices() : List<SujiDevice>{
        return _unassignedSujiDevices.value + athleteDeviceMap.value.values
    }

    override fun clearAthletes() {
        _unassignedAthletes.value = listOf()
    }

    override suspend fun inflateToPercentage(
        deviceName: String,
        percentage: Int
    ) {
        val device = getSujiDevice(deviceName)
        if(device != null){
            val maxPressure = when(device.assignedLimb){
                Limb.ARM -> device.lopArm.toFloat()
                Limb.LEG -> device.lopLeg.toFloat()
                null -> return
            }
            val targetPressure = ((maxPressure / 100) * percentage).toInt()


            var pressure = device.pressure

            while(pressure != targetPressure){
                if(pressure < targetPressure){
                    pressure ++
//                    replaceSujiDevice(deviceName, device.copy(inflationStatus = InflationStatus.INFLATING, pressure = pressure))
                    _updateSujiEvent.emit(device.copy(inflationStatus = InflationStatus.INFLATING, pressure = pressure))
                    delay(5)
                }

                if(pressure > targetPressure){
                    pressure --
                    _updateSujiEvent.emit(device.copy(inflationStatus = InflationStatus.DEFLATING, pressure = pressure))
//                    replaceSujiDevice(deviceName, device.copy(inflationStatus = InflationStatus.DEFLATING, pressure = pressure))
                    delay(5)
                }
            }
            _updateSujiEvent.emit(device.copy(inflationStatus = InflationStatus.INFLATED, pressure = pressure))
            replaceSujiDevice(deviceName, device.copy(inflationStatus = InflationStatus.INFLATED, pressure = pressure) )

        }

    }

    override fun replaceSujiDevice(
        oldSujiDeviceName: String,
        newSujiDevice: SujiDevice
    ) {

        var sujiDevice = getSujiDevice(oldSujiDeviceName)
        if(isSujiDeviceConnected(oldSujiDeviceName)){
            var inverse = athleteDeviceMap.value.inverse()
            val athlete = inverse[getSujiDevice(oldSujiDeviceName)]
            val mapCopy = athleteDeviceMap.value.toMutableMap()
            mapCopy.remove(athlete)
            mapCopy.put(athlete, newSujiDevice)
            _athleteDeviceMap.value =  HashBiMap.create<Athlete, SujiDevice>(mapCopy)
        } else {
            val listCopy = unassignedSujiDevices.value.toMutableList()
            listCopy.remove(sujiDevice)
            listCopy.add(newSujiDevice)
            _unassignedSujiDevices.value = listCopy.toList()
        }

    }

    override suspend fun connectSujiToAthlete(
        athleteUID: String,
        deviceName: String
    ): Boolean {
        if(getAthlete(athleteUID) == null || getSujiDevice(deviceName) == null){
            return false
        } else {
            val athlete = getAthlete(athleteUID)
            val lopArm = athlete!!.lopArm
            val lopLeg = athlete!!.lopLeg

            if(!isSujiDeviceConnected(deviceName) && !isAthleteConnected(athleteUID)){
                replaceSujiDevice(deviceName, getSujiDevice(deviceName)!!.copy(connectionStatus = ConnectionStatus.CONNECTING))
                val device = getSujiDevice(deviceName)!!
                val mapCopy = _athleteDeviceMap.value.toMutableMap()
                mapCopy.put(getAthlete(athleteUID), getSujiDevice(deviceName))
                _athleteDeviceMap.value =  HashBiMap.create<Athlete, SujiDevice>(mapCopy)
                var listCopy = unassignedSujiDevices.value.toMutableList()
                listCopy.remove(getSujiDevice(deviceName))
                _unassignedSujiDevices.value = listCopy
                var listCopy2 = unassignedAthletes.value.toMutableList()
                listCopy2.remove(getAthlete(athleteUID))
                _unassignedAthletes.value = listCopy2
                _updateSujiEvent.emit(device.copy(connectionStatus = ConnectionStatus.CONNECTING))
                delay(4000)
                _updateSujiEvent.emit(device.copy(connectionStatus = ConnectionStatus.CONNECTED, lopLeg = lopLeg, lopArm = lopArm))
                replaceSujiDevice(deviceName, getSujiDevice(deviceName)!!.copy(connectionStatus = ConnectionStatus.CONNECTED, lopArm = lopArm, lopLeg = lopLeg))
                return true
            } else {
                val first = getAthlete(athleteUID)!!
                val second = getAthlete(athleteDeviceMap.value.inverse()[getSujiDevice(deviceName)]!!.uid)!!
                reassignEvent(first, second)
                return false
            }
        }
    }

    override suspend fun disconnectSujiFromAthlete(
        deviceName: String,
        athleteUID: String
    ): Boolean {
        if(getAthlete(athleteUID) == null || getSujiDevice(deviceName) == null){
            return false
        } else {
            val athlete = getAthlete(athleteUID)
            val device = getSujiDevice(deviceName = deviceName)
                replaceSujiDevice(deviceName, getSujiDevice(deviceName = deviceName)!!.copy(connectionStatus = ConnectionStatus.DISCONNECTING))
                delay(500)
                val athListCopy = _unassignedAthletes.value.toMutableList()
                athListCopy.add(athlete!!)
                _unassignedAthletes.value = athListCopy
                val sujiListCopy = _unassignedSujiDevices.value.toMutableList()
                sujiListCopy.add(device!!)
                _unassignedSujiDevices.value = sujiListCopy
                val mapCopy =  athleteDeviceMap.value.toMutableMap()
                mapCopy.remove(athlete)
                _athleteDeviceMap.value = HashBiMap.create(mapCopy)
                replaceSujiDevice(getSujiDevice(deviceName)!!.name, getSujiDevice(deviceName = deviceName)!!.copy(connectionStatus = ConnectionStatus.DISCONNECTED))
                return true

        }
    }

    override fun caliabrateToLimb(
        deviceName: String,
        limb: Limb
    ) {
        val device = getSujiDevice(deviceName)
        if (device != null) {
            replaceSujiDevice(deviceName, device.copy(assignedLimb = limb))
        }
    }
}
