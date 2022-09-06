package com.suji.sujitechnicalexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.google.firebase.FirebaseApp
import com.suji.userinterface.theme.SujiTechnicalExerciseTheme
import com.suji.userinterface.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main (and only) activity for the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SujiTechnicalExerciseTheme() {
                    FirebaseApp.initializeApp(this)
                    Navigation()
            }
        }
    }
}

