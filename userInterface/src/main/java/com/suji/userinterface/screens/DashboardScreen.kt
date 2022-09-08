package com.suji.userinterface.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.suji.domain.model.Athlete
import com.suji.domain.model.DashboardBottomDrawers
import com.suji.domain.model.InflationStatus
import com.suji.domain.model.Limb
import com.suji.userinterface.R
import com.suji.userinterface.components.bottomDrawers.FilterSelector
import com.suji.userinterface.components.cards.AthleteCard
import com.suji.userinterface.components.cards.AthleteCardPlaceholder
import com.suji.userinterface.components.controlPanels.SujiControlPanel
import com.suji.userinterface.components.filters.DarkFilter
import com.suji.userinterface.components.lists.PagingList
import com.suji.userinterface.components.bottomDrawers.LimbSelector
import com.suji.userinterface.components.bottomDrawers.ReassignSuji
import com.suji.userinterface.components.bottomDrawers.SujiDeviceSelector
import com.suji.userinterface.theme.dimensions
import com.suji.userinterface.viewModels.DashboardViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A screen to manage and control Suji devices for multiple athletes
 * @param navController Controls navigation between screens
 * @param viewModel Manages the state for the screen
 */
@OptIn(
    ExperimentalMaterialApi::class,
)
@Preview
@Composable
fun DashboardScreen(
    navController: NavController = rememberNavController(),
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val color = MaterialTheme.colors
    val state = viewModel.state

    val athleteDeviceMapState = state.athleteDeviceMap.collectAsState()
    val unassignedSujiDeviceState = state.unassignedSujiDevices.collectAsState()


    //Expands / contracts bottom drawer when Drawer state changes
    LaunchedEffect(
        key1 = state.bottomDrawer.value,
    ) {
        if (state.bottomDrawer.value != DashboardBottomDrawers.NONE) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    //Contains composables for each possible bottom drawer
    val bottomSheet = @Composable {
        when (state.bottomDrawer.value) {
            DashboardBottomDrawers.SELECT_SUJI -> {
                SujiDeviceSelector(
                    sujiDeviceList = (unassignedSujiDeviceState.value + athleteDeviceMapState.value.values).sortedBy { device -> device.name },
                    selectSujiDevice = { device ->
                        state.bottomDrawer.value = DashboardBottomDrawers.NONE
                        viewModel.connectAthleteToSujiDevice(device.name, state.selectedAthlete.value!!.uid)
                    },
                    scanForDevices = { viewModel.scanForDevices() }
                )
            }

            DashboardBottomDrawers.SELECT_LIMB -> {
                LimbSelector(
                    sujiDevice = viewModel.state.selectedSujiDevice.value!!,
                    selectLimb = {
                        viewModel.calibrateToLimb(deviceName = state.selectedSujiDevice.value!!.name, it)
                        state.bottomDrawer.value = DashboardBottomDrawers.NONE
                    }
                )
            }

            DashboardBottomDrawers.REASSIGN_ATHLETES -> {
                val newAthlete = state.reassignPair.value!!.first
                val oldAthlete = state.reassignPair.value!!.second
                ReassignSuji(
                    oldAthlete = oldAthlete,
                    newAthlete = newAthlete,
                    reassign = {
                        viewModel.reassign(
                            state.athleteDeviceMap.value[oldAthlete]!!.name,
                            oldAthlete.uid,
                            newAthlete.uid,
                        )
                        state.bottomDrawer.value = DashboardBottomDrawers.NONE
                    },
                    enabled = athleteDeviceMapState.value[oldAthlete]!!.inflationStatus != InflationStatus.INFLATING
                )
            }

            DashboardBottomDrawers.SUJI_CONTROL_PANEL -> {

                val athlete = state.selectedAthlete.value!!
                val device = athleteDeviceMapState.value[athlete]!!
                SujiControlPanel(
                    onStopAndDeflate = { viewModel.inflateSujiDeviceToPercentage(device.name, 0 ) },
                    onSelectLimb = {
                        state.selectedSujiDevice.value = device
                        state.bottomDrawer.value = DashboardBottomDrawers.SELECT_LIMB
                    },
                    onInflateToPercentage = { percentage ->
                        viewModel.inflateSujiDeviceToPercentage(
                            device.name,
                            percentage
                        )
                    },
                    sujiDevice = device,
                    athlete = athlete,
                    onDisconnect = {
                        state.bottomDrawer.value = DashboardBottomDrawers.NONE
                        viewModel.disconnect(
                            device.name,
                            athleteUID = athlete.uid
                        )
                    }
                )
            }

            DashboardBottomDrawers.FILTER -> {
                FilterSelector(
                    filtered = state.filtered.value,
                    onToggleFilterConnected = {state.filtered.value = !state.filtered.value},
                    filteredByLimb = state.limbFilter.value,
                    onFilterLimb = {limb -> state.limbFilter.value = limb

                    }
                )
            }

            else -> {
                Spacer(modifier = Modifier.height(180.dp))

            }
        }
    }

    //Contains the main content in the screen
    val content = @Composable {
        var list : List<Athlete> = if(state.filtered.value){
            athleteDeviceMapState.value.keys.toList()
        } else {
            athleteDeviceMapState.value.keys.toList()  + state.unassignedAthletes.collectAsState().value.toList()
        }

        when(state.limbFilter.value){
            Limb.ARM -> list = list.filter { athlete -> state.athleteDeviceMap.value[athlete]?.assignedLimb == Limb.ARM }
            Limb.LEG -> list = list.filter { athlete -> state.athleteDeviceMap.value[athlete]?.assignedLimb == Limb.LEG }
            else -> {}
        }


        Box() {
            Column(Modifier.padding(horizontal = 12.dp)) {
                PagingList<Athlete>(
                    Heading = {
                        Spacer(modifier = Modifier.height(dimensions.small))
                    },
                    list = (list.sortedBy { athlete -> athlete.name  }),
                    endReached = state.endReached.collectAsState().value,
                    isLoading = state.isLoading.collectAsState().value,
                    loadNextPage = { viewModel.loadNextAthletePage() },
                    refresh = { viewModel.refresh() },
                    swipeState = state.swipeRefreshState.value,
                    content = { athlete ->
                        val device = athleteDeviceMapState.value[athlete]
                        if (!state.isRefreshing.collectAsState().value) {
                            AthleteCard(
                                athlete = athlete,
                                onSelectDeviceClicked = {
                                    viewModel.selectAthlete(it)
                                    state.bottomDrawer.value =
                                        DashboardBottomDrawers.SELECT_SUJI
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                },
                                sujiDevice = device,
                                onClick = {
                                    state.selectedSujiDevice.value = it
                                    state.selectedAthlete.value = athlete
                                    state.bottomDrawer.value =
                                        DashboardBottomDrawers.SUJI_CONTROL_PANEL
                                },
                                onAthleteClicked = {
                                    navController.navigate("profile")
                                }
                            )
                        }
                    },
                    placeholderContent = {
                        if (!state.isRefreshing.collectAsState().value) {
                            AthleteCardPlaceholder()
                        } else {
                            AthleteCardPlaceholder()
                            AthleteCardPlaceholder()
                            AthleteCardPlaceholder()
                            AthleteCardPlaceholder()
                        }
                    },
                )
            }
            DarkFilter(
                visible = state.bottomDrawer.value != DashboardBottomDrawers.NONE,
                onClick = {
                    state.bottomDrawer.value = DashboardBottomDrawers.NONE;
                })
        }
    }

    //Top Bar Composable
    val topBar = @Composable {
        Box(Modifier.height(50.dp)) {
            TopAppBar(
                title = { Text(text = "AT Dashboard") },
                backgroundColor = color.surface,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = dimensions.elevation,
                actions = {
                    IconButton(onClick = { state.bottomDrawer.value = DashboardBottomDrawers.FILTER }) {
                        Icon(painter = painterResource(id = R.drawable.filter), "Filter")
                    }
                }
            )
            DarkFilter(
                visible = state.bottomDrawer.value != DashboardBottomDrawers.NONE,
                onClick = {
                    state.bottomDrawer.value = DashboardBottomDrawers.NONE;
                })
        }
    }

    /**
     * Scaffold that combines top bar, content, bottom drawer and bottom drawer
     */
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContentColor = color.background,
        sheetContent = { bottomSheet() },
        content = { content() },
        sheetPeekHeight = 0.dp,
        drawerGesturesEnabled = false,
        sheetGesturesEnabled = false,
        topBar = {
            topBar()
        },
        sheetShape = RoundedCornerShape(
            dimensions.small,
            dimensions.small,
            0.dp,
            0.dp
        )
    )
}
