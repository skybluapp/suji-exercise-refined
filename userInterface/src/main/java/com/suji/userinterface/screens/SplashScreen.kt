package com.suji.userinterface.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.suji.userinterface.R
import kotlinx.coroutines.delay


/**
 * A splash screen to display when the app is loading
 * @param navController Controls navigation between screens
 */
@Composable
fun SplashScreen(navController : NavController){

    val color = MaterialTheme.colors

    //After 1s, navigate to dashboard screen
    LaunchedEffect(key1 = true) {
        delay(1000)
        navController.navigate("dashboard"){
            popUpTo("splash"){inclusive = true} }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color.background),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_sujisvg),
            contentDescription = "Suji Logo",
            modifier = Modifier.size(200.dp),
            tint = color.primary
        )
    }
}



