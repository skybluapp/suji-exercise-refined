package com.suji.userinterface.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.suji.userinterface.screens.DashboardScreen
import com.suji.userinterface.screens.ProfileScreen
import com.suji.userinterface.screens.SplashScreen
import com.suji.userinterface.viewModels.DashboardViewModel

/**
 * Manages Navigation for the application
 * @param navHostController Manages navigation for the application
 * @param dashboardViewModel Manages state for the dashboard screen
 */
@Composable
fun Navigation(
    navHostController: NavHostController = rememberNavController(),
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        //Splash screen is the first screen to display on app launch
        NavHost(
            navController = navHostController,
            startDestination = "splash"
        ) {

            //Splash Screen Destination
            composable("splash") {
                SplashScreen(navController = navHostController)
            }

            //Dashboard Destination
            composable("dashboard") {
                DashboardScreen(
                    navController = navHostController,
                    viewModel = dashboardViewModel,
                )
            }

            //Dashboard Destination
            composable("profile") {
                ProfileScreen(
                    navController = navHostController,
                )
            }
        }
    }

}

