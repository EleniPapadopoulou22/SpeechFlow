package com.eleni.speechflow.ui.screens.listen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.eleni.speechflow.SpeechFlowApplication
import com.eleni.speechflow.data.local.TranscriptionType
import com.eleni.speechflow.data.repository.SettingsRepository
import com.eleni.speechflow.data.repository.TranscriptionRepository
import com.eleni.speechflow.data.speech.SpeechRecognitionManager
import com.eleni.speechflow.data.vibration.VibrationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListenViewModel(
    private val speechManager: SpeechRecognitionManager,
    private val repository: TranscriptionRepository,
    private val settingsRepository: SettingsRepository,
    private val vibrationManager: VibrationManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ListenUiState())
    val state: StateFlow<ListenUiState> = _state.asStateFlow()

    private var currentLanguage = "el-GR"

    init {
        viewModelScope.launch {
            speechManager.state.collect { rec ->
                _state.value = _state.value.copy(
                    isListening = rec.isListening,
                    partialText = rec.partialText,
                    finalText   = rec.finalText.ifBlank { _state.value.finalText },
                    error       = rec.error,
                )
                if (rec.finalText.isNotBlank() && !_state.value.saved) {
                    repository.save(
                        text = rec.finalText,
                        type = TranscriptionType.LISTEN,
                    )
                    _state.value = _state.value.copy(saved = true)
                    vibrationManager.vibrate(50) // Short buzz on completion
                }
            }
        }

        viewModelScope.launch {
            settingsRepository.language.collect { lang ->
                currentLanguage = lang
            }
        }
    }

    fun setPermission(granted: Boolean) {
        _state.value = _state.value.copy(hasPermission = granted)
    }

    fun startListening() {
        _state.value = _state.value.copy(saved = false, error = null)
        vibrationManager.vibrate(100)
        speechManager.start(currentLanguage)
    }

    fun stopListening() {
        vibrationManager.vibrate(50)
        speechManager.stop()
    }

    fun reset() {
        speechManager.reset()
        _state.value = ListenUiState(hasPermission = _state.value.hasPermission)
    }

    override fun onCleared() {
        speechManager.stop()
        super.onCleared()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SpeechFlowApplication)
                ListenViewModel(
                    speechManager = app.speechRecognitionManager,
                    repository    = app.transcriptionRepository,
                    settingsRepository = app.settingsRepository,
                    vibrationManager = app.vibrationManager,
                )
            }
        }
    }
}
