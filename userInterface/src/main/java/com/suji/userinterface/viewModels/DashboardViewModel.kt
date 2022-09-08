package com.suji.userinterface.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.suji.domain.AthletesPagingInterface
import com.suji.domain.connectedSujiDevices.SujiDeviceManager
import com.suji.domain.model.Athlete
import com.suji.domain.model.DashboardBottomDrawers
import com.suji.domain.model.Limb
import com.suji.domain.model.SujiDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Holds the current state of the Dashboard Screen
 * @param athleteDeviceMap Contains a mapping of assigned athletes and assigned Suji devices
 * @param unassignedAthletes Contains a list of all unassigned athletes
 * @param unassignedSujiDevices Contains a list of all unassigned suji devices
 * @param selectedAthlete Contains an athlete that can be selected
 * @param selectedSujiDevice Contains a suji device that can be selected
 * @param reassignEvent event that is called when the user requests a device be reassigned
 * @param isLoading contains the current loading state of the paged list
 * @param isRefreshing contains the refreshing state of the paged list
 * @param bottomDrawer The current opened bottom drawer
 * @param reassignPair The pair of athletes to be reassigned
 */
data class DashboardState(
    var athleteDeviceMap: StateFlow<BiMap<Athlete, SujiDevice>> = MutableStateFlow(HashBiMap.create()),
    var deviceAthleteMap: MutableState<BiMap<SujiDevice, Athlete>> = mutableStateOf(HashBiMap.create()),
    var unassignedAthletes: StateFlow<List<Athlete>> = MutableStateFlow(listOf()),
    var unassignedSujiDevices: StateFlow<List<SujiDevice>> = MutableStateFlow(listOf()),
    var selectedAthlete: MutableState<Athlete?> = mutableStateOf(null),
    var selectedSujiDevice: MutableState<SujiDevice?> = mutableStateOf(null),
    var reassignEvent: SharedFlow<Pair<Athlete, Athlete>> = MutableSharedFlow<Pair<Athlete, Athlete>>().asSharedFlow(),
    var isLoading: StateFlow<Boolean> = MutableStateFlow(false),
    var endReached: StateFlow<Boolean> = MutableStateFlow(false),
    var isRefreshing: StateFlow<Boolean> = MutableStateFlow(false),
    val bottomDrawer: MutableState<DashboardBottomDrawers> = mutableStateOf(DashboardBottomDrawers.NONE),
    val swipeRefreshState: MutableState<SwipeRefreshState> = mutableStateOf(SwipeRefreshState(isRefreshing = isRefreshing.value)),
    val reassignPair: MutableState<Pair<Athlete, Athlete>?> = mutableStateOf(null),
    val filtered : MutableState<Boolean> = mutableStateOf(false),
    val limbFilter : MutableState<Limb?> = mutableStateOf(null)
)

/**
 * Provides the current state and a means to make requests to domain layer from the UI
 * @param paging Interface to interact with pager for athlete data
 * @param connectedSujiDevices Interface to interact with connectedSujiDevices
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val connectedSujiDevices: SujiDeviceManager,
    private val paging: AthletesPagingInterface
) : ViewModel() {

    //Screen State
    var state by mutableStateOf(DashboardState())

    //Gather state flows from data sources
    //Subscribe to event that informs when a user has attempted to reassign a Suji Device
    //Collect and invert the Athlete to Device map to provide an equivalent mapping from Device to Athlete
    init {
        state.unassignedAthletes = connectedSujiDevices.unassignedAthletes
        state.unassignedSujiDevices = connectedSujiDevices.unassignedSujiDevices
        state.athleteDeviceMap = connectedSujiDevices.athleteDeviceMap
        state.isRefreshing = paging.isRefreshing
        state.isLoading = paging.isLoading
        state.endReached = paging.endReached
        state.reassignEvent = connectedSujiDevices.reassignEvent
        loadNextAthletePage()
        viewModelScope.launch {
            launch {
                state.reassignEvent.collect {
                    state.selectedAthlete.value = it.first
                    state.reassignPair.value = it
                    state.bottomDrawer.value = DashboardBottomDrawers.REASSIGN_ATHLETES
                }
            }
            launch {
                connectedSujiDevices.athleteDeviceMap.collect { athleteDeviceMap ->
                    state.deviceAthleteMap.value = athleteDeviceMap.inverse()
                }
            }

        }
    }

    /**
     * Stores an athlete in state to use later
     * @param athlete The athlete to select
     */
    fun selectAthlete(athlete: Athlete) {
        state.selectedAthlete.value = athlete
    }

    /**
     * Request a connection between the athlete selected in state to a Suji device
     * @param deviceName The device to connect to
     * @param athleteUID The ID of the athlete to connect to the device
     */
    fun connectAthleteToSujiDevice(
        deviceName: String,
        athleteUID: String
    ) {
        viewModelScope.launch {
            connectedSujiDevices.connectSujiToAthlete(
                athleteUID = athleteUID,
                deviceName = deviceName
            )
        }
    }

    /**
     * Request a scan for new Suji devices
     * (For the purposes of this demo, this adds three new devices to the available device list)
     */
    fun scanForDevices() {
        viewModelScope.launch {
            connectedSujiDevices.addSujiDevice(SujiDevice())
            connectedSujiDevices.addSujiDevice(SujiDevice())
            connectedSujiDevices.addSujiDevice(SujiDevice())
        }
    }

    //Calls upon pager to retrieve next page
    fun loadNextAthletePage() {
        viewModelScope.launch {
            paging.requestNextPage()
        }
    }

    // Clears paged data and collects first page
    fun refresh() {
        viewModelScope.launch {
            paging.reset()
        }
    }

    /**
     * Requests a suji device is inflated to a specified percentage
     * @param deviceName The name of the Suji-Device
     * @param targetPercentage The percentage to inflate to
     */
    fun inflateSujiDeviceToPercentage(
        deviceName: String,
        targetPercentage: Int
    ) {
        viewModelScope.launch {
            connectedSujiDevices.inflateToPercentage(
                deviceName,
                targetPercentage
            )
        }
    }

    /**
     * Request reassignment of a suji device to a new athlete
     * @param deviceName The name of the Suji Device to reassign
     * @param oldAthleteUID The UID of the athlete to un-assign
     * @param newAthleteUID The UID of the athlete to assign
     */
    fun reassign(
        deviceName: String,
        oldAthleteUID: String,
        newAthleteUID: String
    ) {
        viewModelScope.launch {
            if (
                connectedSujiDevices.disconnectSujiFromAthlete(
                    deviceName,
                    athleteUID = oldAthleteUID
                )
            ) {
                connectedSujiDevices.connectSujiToAthlete(
                    athleteUID = newAthleteUID,
                    deviceName
                )
            }
        }
    }

    /**
     * Request disconnection of a suji device from a athlete
     * @param deviceName The name of the device to disconnect
     * @param athleteUID The name of the athlete to connect
     */
    fun disconnect(
        deviceName: String,
        athleteUID: String
    ) {
        viewModelScope.launch {
            connectedSujiDevices.disconnectSujiFromAthlete(
                deviceName,
                athleteUID = athleteUID
            )
        }
    }

    /**
     * Requests the calibrated limb of the selected suji device be changed
     * @param deviceName The name of the Suji device to replace the calibrate the limb for
     * @param limb The limb to calibrate the device to
     */
    fun calibrateToLimb(deviceName : String, limb: Limb) {
        var p = state.deviceAthleteMap.value.values.find { device -> device.name == deviceName }
        Timber.d(p.toString())
        connectedSujiDevices.caliabrateToLimb(deviceName, limb)
         p = state.deviceAthleteMap.value.values.find { device -> device.name == deviceName }
        Timber.d(p.toString())
    }
}
