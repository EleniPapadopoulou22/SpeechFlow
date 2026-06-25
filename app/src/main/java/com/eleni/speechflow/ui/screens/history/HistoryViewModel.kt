package com.eleni.speechflow.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.eleni.speechflow.SpeechFlowApplication
import com.eleni.speechflow.data.local.TranscriptionEntity
import com.eleni.speechflow.data.repository.TranscriptionRepository
import com.eleni.speechflow.util.formatDateHeader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer

class HistoryViewModel(
    private val repository: TranscriptionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collect { items ->
                val grouped = items.groupBy { formatDateHeader(it.timestamp) }
                _state.value = HistoryUiState(groupedItems = grouped, isLoading = false)
            }
        }
    }

    fun delete(item: TranscriptionEntity) {
        viewModelScope.launch { repository.delete(item) }
    }

    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SpeechFlowApplication)
                HistoryViewModel(repository = app.transcriptionRepository)
            }
        }
    }
}
