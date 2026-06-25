package com.eleni.speechflow.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import com.eleni.speechflow.SpeechFlowApplication
import com.eleni.speechflow.data.repository.SettingsRepository
import com.eleni.speechflow.data.vibration.VibrationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: SettingsRepository,
    private val vibrationManager: VibrationManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.language.collect { _state.value = _state.value.copy(language = it) }
        }
        viewModelScope.launch {
            repo.fontScale.collect { _state.value = _state.value.copy(fontScale = it) }
        }
        viewModelScope.launch {
            repo.vibration.collect { _state.value = _state.value.copy(vibration = it) }
        }
        viewModelScope.launch {
            repo.speechRate.collect { _state.value = _state.value.copy(speechRate = it) }
        }
        viewModelScope.launch {
            repo.theme.collect { _state.value = _state.value.copy(theme = it) }
        }
    }

    fun setLanguage(v: String)   { viewModelScope.launch { repo.setLanguage(v) } }
    fun setFontScale(v: Float)   { viewModelScope.launch { repo.setFontScale(v) } }
    
    fun setVibration(v: Boolean) { 
        viewModelScope.launch { 
            repo.setVibration(v)
            if (v) vibrationManager.vibrate(50)
        } 
    }
    
    fun setSpeechRate(v: Float)  { viewModelScope.launch { repo.setSpeechRate(v) } }
    fun setTheme(v: String) { viewModelScope.launch { repo.setTheme(v) } }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SpeechFlowApplication)
                SettingsViewModel(
                    repo = app.settingsRepository,
                    vibrationManager = app.vibrationManager
                )
            }
        }
    }
}
