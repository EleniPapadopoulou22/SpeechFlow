package com.eleni.speechflow.ui.screens.history

import com.eleni.speechflow.data.local.TranscriptionEntity

data class HistoryUiState(
    val groupedItems: Map<String, List<TranscriptionEntity>> = emptyMap(),
    val isLoading: Boolean = true,
) {
    val isEmpty: Boolean = groupedItems.isEmpty()
}
