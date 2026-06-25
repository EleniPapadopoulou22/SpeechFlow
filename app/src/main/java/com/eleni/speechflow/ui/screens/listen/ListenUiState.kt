package com.eleni.speechflow.ui.screens.listen

data class ListenUiState(
    val isListening: Boolean = false,
    val partialText: String = "",
    val finalText: String = "",
    val error: String? = null,
    val hasPermission: Boolean = false,
    val saved: Boolean = false,
)
