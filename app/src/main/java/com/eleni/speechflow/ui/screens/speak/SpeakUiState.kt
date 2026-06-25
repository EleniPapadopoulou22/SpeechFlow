package com.eleni.speechflow.ui.screens.speak

data class SpeakUiState(
    val text: String = "",
    val isInitialized: Boolean = false,
    val isSpeaking: Boolean = false,
    val error: String? = null,
)
