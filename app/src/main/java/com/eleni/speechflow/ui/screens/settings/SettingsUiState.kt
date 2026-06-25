package com.eleni.speechflow.ui.screens.settings

data class SettingsUiState(
    val language: String = "el-GR",
    val fontScale: Float = 1.0f,
    val vibration: Boolean = true,
    val speechRate: Float = 1.0f,
    val theme: String = "system",
)
