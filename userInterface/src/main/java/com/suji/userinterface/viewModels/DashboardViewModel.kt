package com.suji.userinterface.viewModels

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.firebase.firestore.DocumentSnapshot
import com.pagination.FirestorePaging
import com.repository.Repository
import com.suji.domain.AthletesPaging
import com.suji.domain.AthletesPagingInterface
import com.suji.model.Athlete
import com.suji.model.ConnectionStatus
import com.suji.model.Limb
import com.suji.model.SujiDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 * Holds the current state of the Dashboard Screen
 * @param isLoading True if the athletes list is currently loading
 * @param endReached True if there is no more content to be loaded from the
 * @param page The document that acts as the key to access more content
 * @param isRefreshing True if content is being refreshed
 * @param swipeRefreshState contains state for swipe-to-refresh
 */
data class DashboardState(
    var isLoading: MutableState<Boolean> = mutableStateOf(false),
    var athletes: MutableList<Athlete> = mutableListOf(),
    var sujiDevices: SnapshotStateList<SujiDevice> = mutableStateListOf(),
    var sujiDeviceFlow: StateFlow<Array<SujiDevice>> = MutableStateFlow(arrayOf()),
    var mapFlow: StateFlow<Map<Athlete, SujiDevice?>> = MutableStateFlow(mapOf()),
    var selectedAthlete: MutableState<Athlete?> = mutableStateOf(null),
    var error: String? = null,
    var endReached: MutableState<Boolean> = mutableStateOf(false),
    var page: DocumentSnapshot? = null,
    val isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    val bottomDrawer: MutableState<DashboardBottomDrawers> = mutableStateOf(DashboardBottomDrawers.NONE),
    val swipeRefreshState: MutableState<SwipeRefreshState> = mutableStateOf(SwipeRefreshState(isRefreshing = isRefreshing.value)),
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: Repository,
    val paging : AthletesPagingInterface
) : ViewModel() {

    //Data Sources
    private val firestore = repository.readServerInterface
    private val connectedSujiDevices = repository.connectedSujiDevicesInterface

    //Screen State
    var state by mutableStateOf(DashboardState())

    //Gather state flows from data sources
    init {
        state.sujiDeviceFlow = connectedSujiDevices.sujiDeviceFlow
        state.mapFlow = connectedSujiDevices.sujiAthleteMapFlow
        viewModelScope.launch {
            launch {
                paging.isRefreshing.collect{
                    state.isRefreshing.value = it
                }
            }
            launch {
                paging.isLoading.collect{
                    state.isLoading.value = it
                }
            }
            launch {
                paging.endReached.collect{
                    state.endReached.value = it
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
     */
    fun connectSelectedAthleteToSujiDevice(deviceName: String) {
        viewModelScope.launch{
            connectedSujiDevices.connectSujiToAthlete(athlete = state.selectedAthlete.value!!, deviceName)
        }
    }

    /**
     * Request a scan for new Suji devices
     * (For the purposes of this demo, this adds three new devices to the available device list)
     */
    fun scanForDevices() {
        viewModelScope.launch {
            connectedSujiDevices.scanForDevices()
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
        connectedSujiDevices.sujiAthleteMapFlow.value.forEach { athleteDevice ->
            if (athleteDevice.value?.connectionStatus != ConnectionStatus.CONNECTED) {
                connectedSujiDevices.removeAthlete(athleteDevice.key)
            }
        }
        //state.page = null

        viewModelScope.launch {
            paging.reset()
        }
    }

    fun inflateSujiDeviceToPercentage(
        deviceName: String,
        targetPercentage: Int
    ) {
        viewModelScope.launch {
            //connectedSujiDevices.getSujiDeviceByName(deviceName)?.inflateToPercentage(targetPercentage)
        }
    }

    fun stopAndDeflate(athleteName: String) {
//        viewModelScope.launch {
//            val device = connectedSujiDevices.getSujiDeviceFromAthlete(athleteName)
//            device?.inflateToPercentage(0)
//            device?.disconnectAthlete()
//            connectedSujiDevices.disconnectSujiFromAthlete(athleteName)
//        }
    }

    fun selectLimb(it: Limb) {

    }


}


enum class DashboardBottomDrawers {
    SELECT_SUJI,
    SELECT_LIMB,
    NONE
}