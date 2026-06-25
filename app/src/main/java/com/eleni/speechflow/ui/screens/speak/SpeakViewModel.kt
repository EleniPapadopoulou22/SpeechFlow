package com.eleni.speechflow.ui.screens.speak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.eleni.speechflow.SpeechFlowApplication
import com.eleni.speechflow.data.local.TranscriptionType
import com.eleni.speechflow.data.repository.SettingsRepository
import com.eleni.speechflow.data.repository.TranscriptionRepository
import com.eleni.speechflow.data.speech.TextToSpeechManager
import com.eleni.speechflow.data.vibration.VibrationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer

class SpeakViewModel(
    private val ttsManager: TextToSpeechManager,
    private val repository: TranscriptionRepository,
    private val settingsRepository: SettingsRepository,
    private val vibrationManager: VibrationManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SpeakUiState())
    val state: StateFlow<SpeakUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            ttsManager.state.collect { tts ->
                _state.value = _state.value.copy(
                    isInitialized = tts.isInitialized,
                    isSpeaking    = tts.isSpeaking,
                    error         = tts.error,
                )
            }
        }
    }

    fun onTextChange(text: String) {
        _state.value = _state.value.copy(text = text)
    }

    fun speak() {
        val text = _state.value.text.trim()
        if (text.isBlank()) return
        vibrationManager.vibrate(50)
        ttsManager.speak(text)
        viewModelScope.launch {
            repository.save(text = text, type = TranscriptionType.SPEAK)
        }
    }

    fun speakPhrase(phrase: String) {
        vibrationManager.vibrate(50)
        ttsManager.speak(phrase)
        viewModelScope.launch {
            repository.save(text = phrase, type = TranscriptionType.SPEAK)
        }
    }

    fun stop() {
        vibrationManager.vibrate(30)
        ttsManager.stop()
    }

    fun clearText() {
        _state.value = _state.value.copy(text = "")
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SpeechFlowApplication)
                SpeakViewModel(
                    ttsManager = app.textToSpeechManager,
                    repository = app.transcriptionRepository,
                    settingsRepository = app.settingsRepository,
                    vibrationManager = app.vibrationManager,
                )
            }
        }
    }
}
