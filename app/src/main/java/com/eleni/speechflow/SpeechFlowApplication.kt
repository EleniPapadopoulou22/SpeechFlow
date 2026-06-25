package com.eleni.speechflow

import android.app.Application
import com.eleni.speechflow.data.local.AppDatabase
import com.eleni.speechflow.data.preferences.SettingsDataStore
import com.eleni.speechflow.data.repository.SettingsRepository
import com.eleni.speechflow.data.repository.TranscriptionRepository
import com.eleni.speechflow.data.speech.SpeechRecognitionManager
import com.eleni.speechflow.data.speech.TextToSpeechManager
import com.eleni.speechflow.data.vibration.VibrationManager

// Manual dependency container for app-wide services and repositories.
class SpeechFlowApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    val transcriptionRepository: TranscriptionRepository by lazy {
        TranscriptionRepository(database.transcriptionDao())
    }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(SettingsDataStore(this))
    }

    val speechRecognitionManager: SpeechRecognitionManager by lazy {
        SpeechRecognitionManager(this)
    }

    val textToSpeechManager: TextToSpeechManager by lazy {
        TextToSpeechManager(this, settingsRepository)
    }

    val vibrationManager: VibrationManager by lazy {
        VibrationManager(this, settingsRepository)
    }

    override fun onTerminate() {
        super.onTerminate()
        // Clean up speech and TTS resources on app shutdown.
        textToSpeechManager.shutdown()
        speechRecognitionManager.stop()
    }
}
