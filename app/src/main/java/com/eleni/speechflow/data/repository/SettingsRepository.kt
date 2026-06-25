package com.eleni.speechflow.data.repository

import com.eleni.speechflow.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dataStore: SettingsDataStore) {

    val language: Flow<String> = dataStore.language
    val fontScale: Flow<Float> = dataStore.fontScale
    val vibration: Flow<Boolean> = dataStore.vibration
    val onboardingDone: Flow<Boolean> = dataStore.onboardingDone
    val speechRate: Flow<Float> = dataStore.speechRate
    val theme: Flow<String> = dataStore.theme

    suspend fun setLanguage(value: String) = dataStore.setLanguage(value)
    suspend fun setFontScale(value: Float) = dataStore.setFontScale(value)
    suspend fun setVibration(value: Boolean) = dataStore.setVibration(value)

    suspend fun setTheme(value: String) = dataStore.setTheme(value)
    suspend fun completeOnboarding() = dataStore.setOnboardingDone(true)
    suspend fun setSpeechRate(value: Float) = dataStore.setSpeechRate(value)
}
