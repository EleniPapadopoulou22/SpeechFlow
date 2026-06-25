package com.eleni.speechflow.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "speechflow_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val LANGUAGE        = stringPreferencesKey("language")
        val FONT_SCALE      = floatPreferencesKey("font_scale")
        val VIBRATION       = booleanPreferencesKey("vibration")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val SPEECH_RATE     = floatPreferencesKey("speech_rate")
        val THEME           = stringPreferencesKey("theme")
    }

    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "el-GR" }
    val fontScale: Flow<Float> = context.dataStore.data.map { it[FONT_SCALE] ?: 1.0f }
    val vibration: Flow<Boolean> = context.dataStore.data.map { it[VIBRATION] ?: true }
    val onboardingDone: Flow<Boolean> = context.dataStore.data.map { it[ONBOARDING_DONE] ?: false }
    val speechRate: Flow<Float> = context.dataStore.data.map { it[SPEECH_RATE] ?: 1.0f }
    val theme: Flow<String> = context.dataStore.data.map { it[THEME] ?: "system" }

    suspend fun setLanguage(value: String) { context.dataStore.edit { it[LANGUAGE] = value } }
    suspend fun setFontScale(value: Float) { context.dataStore.edit { it[FONT_SCALE] = value } }
    suspend fun setVibration(value: Boolean) { context.dataStore.edit { it[VIBRATION] = value } }
    suspend fun setOnboardingDone(value: Boolean) { context.dataStore.edit { it[ONBOARDING_DONE] = value } }
    suspend fun setSpeechRate(value: Float) { context.dataStore.edit { it[SPEECH_RATE] = value } }
    suspend fun setTheme(value: String) { context.dataStore.edit { it[THEME] = value } }
}
