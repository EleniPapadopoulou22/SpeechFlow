package com.eleni.speechflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eleni.speechflow.ui.screens.history.HistoryScreen
import com.eleni.speechflow.ui.screens.home.HomeScreen
import com.eleni.speechflow.ui.screens.listen.ListenScreen
import com.eleni.speechflow.ui.screens.onboarding.OnboardingScreen
import com.eleni.speechflow.ui.screens.settings.SettingsScreen
import com.eleni.speechflow.ui.screens.speak.SpeakScreen

@Composable
fun SpeechFlowNavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onListenClick   = { navController.navigate(Screen.Listen.route) },
                onSpeakClick    = { navController.navigate(Screen.Speak.route) },
                onHistoryClick  = { navController.navigate(Screen.History.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
            )
        }

        composable(Screen.Listen.route) {
            ListenScreen(
                onBack = { navController.popBackStack() },
                onAnswerClick = { navController.navigate(Screen.Speak.route) }
            )
        }

        composable(Screen.Speak.route) {
            SpeakScreen(
                onAnswerClick = { navController.navigate(Screen.Listen.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

    }
}
