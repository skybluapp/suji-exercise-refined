package com.suji.userinterface.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.suji.userinterface.R
import com.suji.userinterface.theme.dimensions

/**
 * A splash screen to display when the app is loading
 * @param navController Controls navigation between screens
 */
@Composable
fun ProfileScreen(navController: NavController) {

    val color = MaterialTheme.colors

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Athlete Profile") },
                backgroundColor = color.surface,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = dimensions.elevation,
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }) {

    }
}

