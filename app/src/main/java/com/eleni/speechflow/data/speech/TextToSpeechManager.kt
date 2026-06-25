package com.eleni.speechflow.data.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.eleni.speechflow.data.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class TextToSpeechManager(context: Context, private val settingsRepository: SettingsRepository) {

    private val _state = MutableStateFlow(TtsState())
    val state: StateFlow<TtsState> = _state.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var currentRate = 1.0f
    private var currentLanguage = "el-GR"

    private val tts: TextToSpeech = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            setupTts()
        } else {
            _state.value = _state.value.copy(error = "Αδυναμία αρχικοποίησης TTS")
        }
    }

    init {

        scope.launch {
            settingsRepository.speechRate.collect { rate ->
                currentRate = rate
                if (_state.value.isInitialized) {
                    tts.setSpeechRate(rate)
                }
            }
        }


        scope.launch {
            settingsRepository.language.collect { lang ->
                currentLanguage = lang
                if (_state.value.isInitialized) {
                    updateLanguage(lang)
                }
            }
        }
    }

    private fun setupTts() {
        updateLanguage(currentLanguage)
        tts.setSpeechRate(currentRate)
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String?) {
                _state.value = _state.value.copy(isSpeaking = true)
            }
            override fun onDone(id: String?) {
                _state.value = _state.value.copy(isSpeaking = false)
            }
            @Deprecated("Deprecated in Java")
            override fun onError(id: String?) {
                _state.value = _state.value.copy(isSpeaking = false, error = "Σφάλμα εκφώνησης")
            }
        })
        _state.value = _state.value.copy(isInitialized = true)
    }

    private fun updateLanguage(langTag: String) {
        val locale = if (langTag == "en-US") Locale.US else Locale("el", "GR")
        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            _state.value = _state.value.copy(error = "Η γλώσσα δεν υποστηρίζεται")
        }
    }

    fun speak(text: String) {
        if (text.isBlank()) return
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SPEECHFLOW_UTT")
    }

    fun stop() {
        tts.stop()
        _state.value = _state.value.copy(isSpeaking = false)
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}

data class TtsState(
    val isInitialized: Boolean = false,
    val isSpeaking: Boolean = false,
    val error: String? = null,
)
