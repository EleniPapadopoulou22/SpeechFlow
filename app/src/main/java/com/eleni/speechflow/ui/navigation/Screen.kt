package com.eleni.speechflow.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home       : Screen("home")
    data object Listen     : Screen("listen")
    data object Speak      : Screen("speak")
    data object History    : Screen("history")
    data object Settings   : Screen("settings")
}
