package com.suji.userinterface.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.suji.model.Athlete
import com.suji.model.SujiDevice
import com.suji.userinterface.components.cards.AthleteCard
import com.suji.userinterface.components.cards.AthleteCardPlaceholder
import com.suji.userinterface.components.cards.SujiDeviceCard
import com.suji.userinterface.components.cards.noRippleClickable
import com.suji.userinterface.components.filters.DarkFilter
import com.suji.userinterface.components.lists.PagingList
import com.suji.userinterface.components.selectors.LimbSelector
import com.suji.userinterface.viewModels.DashboardBottomDrawers
import com.suji.userinterface.viewModels.DashboardViewModel
import kotlinx.coroutines.launch

/**
 * A screen to manage and control Suji devices for multiple athletes
 * @param navController Controls navigation between screens
 * @param viewModel Manages the state for the screen
 */
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalCoilApi::class,
    ExperimentalAnimationApi::class
)
@Preview
@Composable
fun DashboardScreen(
    navController: NavController = rememberNavController(),
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val scaffoldState =
        rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val color = MaterialTheme.colors
    val state = viewModel.state
    LaunchedEffect(true) {
        viewModel.loadNextAthletePage()
    }

    val context = LocalContext.current


    LaunchedEffect(
        key1 = state.bottomDrawer.value,
    ) {
        if (state.bottomDrawer.value != DashboardBottomDrawers.NONE) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.collapse()
        }

    }



    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContentColor = color.background,
        sheetContent = {

            when (state.bottomDrawer.value) {
                DashboardBottomDrawers.SELECT_SUJI -> {
                    SujiDeviceSelector(
                        sujiDeviceList = state.sujiDeviceFlow.collectAsState().value.toList(),
                        selectSujiDevice = {
                            state.bottomDrawer.value = DashboardBottomDrawers.NONE
                            viewModel.connectSelectedAthleteToSujiDevice(it)
                        },
                        map = viewModel.state.mapFlow.collectAsState().value,
                        scanForDevices = { viewModel.scanForDevices() }
                    )
                }

                DashboardBottomDrawers.SELECT_LIMB -> {
                    LimbSelector(
                        selectLimb = {
                            state.bottomDrawer.value = DashboardBottomDrawers.SELECT_LIMB
                        }
                    )
                }

                else -> {
                    Spacer(modifier = Modifier.height(180.dp))

                }
            }

        },
        content = {
            Box {

                Column(Modifier.padding(horizontal = 12.dp)) {
                    PagingList<Athlete>(
                        Heading = {
                            Spacer(modifier = Modifier.height(6.dp))
                        },
                        list = state.mapFlow.collectAsState().value.keys.toMutableList()
                            .sortedBy { athlete -> athlete.name },
                        endReached = state.endReached.value,
                        isLoading = state.isLoading.value,
                        loadNextPage = { viewModel.loadNextAthletePage() },
                        refresh = {
                            state.isRefreshing.value = true
                            viewModel.refresh()
                            context.imageLoader.diskCache?.clear()
                            context.imageLoader.memoryCache?.clear()
                        },
                        swipeState = state.swipeRefreshState.value,
                        content = { athlete ->
                            val device = state.mapFlow.collectAsState().value[athlete]

                            if (!state.isRefreshing.value) {
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
                                    inflateToPercentage = {
                                        if (device != null) {
                                            viewModel.inflateSujiDeviceToPercentage(
                                                device.name,
                                                it
                                            )
                                        }
                                    },
                                    stopAndDeflate = {
                                        if (device != null) {
                                            viewModel.stopAndDeflate(athlete.name)
                                        }
                                    },
                                    openSelectLimb = {
                                        state.bottomDrawer.value =
                                            DashboardBottomDrawers.SELECT_LIMB
                                    }
                                )
                            }
                        },
                        placeholderContent = {
                            AthleteCardPlaceholder()
                            AthleteCardPlaceholder()
                            AthleteCardPlaceholder()
                            //CircularProgressIndicator()
                        }
                    )
                }

                DarkFilter(
                    visible = state.bottomDrawer.value != DashboardBottomDrawers.NONE,
                    onClick = {
                        state.bottomDrawer.value = DashboardBottomDrawers.NONE;
                        scope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    })

            }

        },
        sheetPeekHeight = 0.dp,
        drawerGesturesEnabled = false,
        sheetGesturesEnabled = false,
        sheetShape = RoundedCornerShape(
            6.dp,
            6.dp,
            0.dp,
            0.dp
        )
    )
}

@Composable
fun SujiDeviceSelector(
    sujiDeviceList: List<SujiDevice>,
    selectSujiDevice: (String) -> Unit,
    map: Map<Athlete, SujiDevice?>,
    scanForDevices: () -> Unit
) {
    val colors = MaterialTheme.colors
    Column(Modifier.background(colors.background)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Select Suji Device",
                color = colors.onBackground,
                style = typography.h6,
                modifier = Modifier.padding(12.dp)
            )
            Text(
                "Scan for devices",
                color = colors.primary,
                style = typography.body2,
                modifier = Modifier
                    .padding(12.dp)
                    .noRippleClickable {
                        scanForDevices()
                    },
            )
        }

        LazyRow(
            Modifier
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            for (sujiDevice in sujiDeviceList) {
                val p = map.filter { it -> it.value == sujiDevice }
                item {
                    SujiDeviceCard(
                        sujiDevice = sujiDevice,
                        onClick = { selectSujiDevice(it) },
                        athlete = p.keys.firstOrNull()
                    )
                }
            }
        }
    }
}

