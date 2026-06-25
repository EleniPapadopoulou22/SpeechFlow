package com.eleni.speechflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.eleni.speechflow.ui.navigation.Screen
import com.eleni.speechflow.ui.navigation.SpeechFlowNavGraph
import com.eleni.speechflow.ui.theme.SpeechFlowTheme

// Main entry point for the app UI and navigation.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as SpeechFlowApplication

        setContent {
            // Read saved settings and render the Compose navigation graph.
            val theme by app.settingsRepository.theme.collectAsState(initial = "system")
            val fontScale by app.settingsRepository.fontScale.collectAsState(initial = 1.0f)
            val onboardingDone by app.settingsRepository.onboardingDone.collectAsState(initial = null)

            SpeechFlowTheme(theme = theme, fontScale = fontScale) {
                if (onboardingDone != null) {
                    val startDestination = if (onboardingDone == true) Screen.Home.route else Screen.Onboarding.route
                    val navController = rememberNavController()
                    SpeechFlowNavGraph(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}
